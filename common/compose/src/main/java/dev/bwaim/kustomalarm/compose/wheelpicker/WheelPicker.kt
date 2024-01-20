/*
 * Copyright 2023 Dev Bwaim team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalSnapperApi::class)

package dev.bwaim.kustomalarm.compose.wheelpicker

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.Layout
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.LazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.SnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.collections.immutable.PersistentList
import kotlin.math.absoluteValue

@Composable
public fun <T : Any> WheelPicker(
    items: PersistentList<T>,
    modifier: Modifier = Modifier,
    nbVisibleItems: Int = 3,
    startIndex: Int = 0,
    onValueChanged: (T) -> Unit = {},
    elementContent: @Composable (T, Modifier) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val layoutInfo = rememberLazyListSnapperLayoutInfo(lazyListState = lazyListState)

    LaunchedEffect(Unit) { lazyListState.scrollToItem(calculateFirstIndex(startIndex, items.size)) }

    LaunchedEffect(lazyListState.isScrollInProgress, onValueChanged) {
        if (!lazyListState.isScrollInProgress) {
            layoutInfo.currentItem?.let { item ->
                val selectedIndex = item.index % items.size
                val selectedItem = items[selectedIndex]
                onValueChanged(selectedItem)
            }
        }
    }

    MyWheel(
        items = items,
        modifier = modifier,
        nbVisibleItems = nbVisibleItems,
        state = lazyListState,
        snapperLayoutInfo = layoutInfo,
        flingBehavior = rememberSnapperFlingBehavior(layoutInfo = layoutInfo),
        content = elementContent,
    )
}

@Composable
private fun <T : Any> MyWheel(
    items: PersistentList<T>,
    nbVisibleItems: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    snapperLayoutInfo: LazyListSnapperLayoutInfo = rememberLazyListSnapperLayoutInfo(lazyListState = state),
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    content: @Composable (T, Modifier) -> Unit,
) {
    val internalContent =
        @Composable {
            content(items[items.lastIndex], Modifier)

            val lazyListLayoutInfo = remember { derivedStateOf { state.layoutInfo } }.value
            val oneItemHeight =
                remember(lazyListLayoutInfo, nbVisibleItems) {
                    oneItemHeight(layoutInfo = lazyListLayoutInfo, nbItems = nbVisibleItems)
                }

            LazyColumn(
                state = state,
                flingBehavior = flingBehavior,
            ) {
                items(
                    count = Int.MAX_VALUE,
                    key = { index -> index },
                ) { index ->
                    val indexCorrected = index % items.size
                    val item = items[indexCorrected]
                    val alpha =
                        calculateAlpha(
                            snapperLayoutInfo = snapperLayoutInfo,
                            oneItemHeight = oneItemHeight,
                            index = index,
                        )
//                    val rotationX =
//                        calculateRotationX(
//                            snapperLayoutInfo = snapperLayoutInfo,
//                            oneItemHeight = oneItemHeight,
//                            index = index,
//                        )

                    val itemModifier =
                        Modifier
                            .alpha(alpha)
                    // https://issuetracker.google.com/issues/275579007 close / open screen to produce it
                    // https://issuetracker.google.com/issues?q=java.lang.IllegalStateException:%20Offset%20is%20unspecified
                    // TODO check with compose update > 1.6.0-beta03
//                            .graphicsLayer { this.rotationX = rotationX }

                    content(item, itemModifier)
                }
            }
        }

    Layout(
        modifier = modifier,
        content = internalContent,
    ) { measurables, constraints ->
        var listMaxHeight = 0
        val placeables =
            measurables.mapIndexed { index, measurable ->
                val modifiedConstraints =
                    when (index) {
                        0 -> constraints
                        else -> constraints.copy(maxHeight = listMaxHeight)
                    }

                val placeable = measurable.measure(modifiedConstraints)

                if (index == 0) {
                    listMaxHeight = placeable.height * nbVisibleItems
                }

                placeable
            }

        val width = placeables[measurables.lastIndex].width
        val height = placeables[measurables.lastIndex].height

        layout(width, height) { placeables[measurables.lastIndex].placeRelative(0, 0) }
    }
}

private fun calculateAlpha(
    snapperLayoutInfo: SnapperLayoutInfo,
    oneItemHeight: Float,
    index: Int,
): Float {
    val distanceToIndexSnap = snapperLayoutInfo.distanceToIndexSnap(index).absoluteValue

    return if (distanceToIndexSnap in 0..oneItemHeight.toInt()) {
        1.2f - (distanceToIndexSnap / oneItemHeight)
    } else {
        0.2f
    }
}

private fun calculateRotationX(
    snapperLayoutInfo: SnapperLayoutInfo,
    oneItemHeight: Float,
    index: Int,
): Float {
    val distanceToIndexSnap = snapperLayoutInfo.distanceToIndexSnap(index)
    val animatedRotationX = -30f * (distanceToIndexSnap / oneItemHeight)

    return if (animatedRotationX.isNaN()) {
        0f
    } else {
        animatedRotationX
    }
}

private fun oneItemHeight(
    layoutInfo: LazyListLayoutInfo,
    nbItems: Int,
): Float {
    return layoutInfo.viewportSize.height.toFloat() / nbItems
}

private fun calculateFirstIndex(
    targetIndex: Int,
    nbItems: Int,
): Int {
    val diff = (Int.MAX_VALUE / 2) % nbItems
    return (Int.MAX_VALUE / 2) + targetIndex - diff - 1
}
