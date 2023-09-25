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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.localisation.R.string

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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@Composable
public fun KaCloseCenterAlignedTopAppBar(
    modifier: Modifier = Modifier,
    onClickNavigation: () -> Unit = {},
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = { CloseNavigationIcon(onClick = onClickNavigation) },
    actions: @Composable RowScope.() -> Unit = {},
) {
    KaCenterAlignedTopAppBar(
        modifier = modifier,
        onClickNavigation = onClickNavigation,
        title = title,
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
            contentDescription = stringResource(id = string.navigation_menu_content_description),
        )
    }
}

@Composable
private fun CloseNavigationIcon(
    onClick: () -> Unit = {},
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = string.navigation_close_content_description),
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

@Composable
@KAlarmPreviews
private fun PreviewKaCloseCenterAlignedTopAppBar() {
    KustomAlarmTheme {
        KaCloseCenterAlignedTopAppBar()
    }
}
