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

package dev.bwaim.kustomalarm.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme

@Composable
public fun KaCenterAlignedTopAppBar(
    modifier: Modifier = Modifier,
    onClickNavigation: () -> Unit = {},
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = { DefaultNavigationIcon(onClick = onClickNavigation) },
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

@Composable
private fun DefaultNavigationIcon(
    onClick: () -> Unit = {},
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = null,
        )
    }
}

@Composable
@KAlarmPreviews
private fun PreviewKaCenterAlignedTopAppBar() {
    KustomAlarmTheme {
        KaCenterAlignedTopAppBar()
    }
}
