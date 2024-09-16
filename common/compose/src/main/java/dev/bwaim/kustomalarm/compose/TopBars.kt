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

@file:OptIn(ExperimentalMaterial3Api::class)

package dev.bwaim.kustomalarm.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.bwaim.kustomalarm.compose.extensions.testIdentifier
import dev.bwaim.kustomalarm.compose.icons.Save
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
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
            ),
    )
}

@Composable
public fun KaBackTopAppBar(
    modifier: Modifier = Modifier,
    onClickNavigation: () -> Unit = {},
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = { BackNavigationIcon(onClick = onClickNavigation) },
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
public fun KaCloseTopAppBar(
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
private fun DefaultNavigationIcon(onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = stringResource(id = string.navigation_menu_content_description),
            modifier = Modifier.testIdentifier("navigation_menu"),
        )
    }
}

@Composable
private fun BackNavigationIcon(onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Filled.ArrowBack,
            contentDescription = stringResource(id = string.navigation_back_content_description),
        )
    }
}

@Composable
private fun CloseNavigationIcon(onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = string.navigation_close_content_description),
        )
    }
}

@Composable
public fun SaveActionIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = stringResource(id = string.global_action_save),
        )
    }
}

@Composable
public fun MoreActionIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = string.global_action_more),
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewKaCenterAlignedTopAppBar() {
    KustomAlarmTheme { KaCenterAlignedTopAppBar() }
}

@Composable
@PreviewsKAlarm
private fun PreviewKaBackCenterAlignedTopAppBar() {
    KustomAlarmTheme {
        KaBackTopAppBar(
            title = { Text(text = "TopBar title ") },
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewKaCloseCenterAlignedTopAppBar() {
    KustomAlarmTheme {
        KaCloseTopAppBar(
            title = { Text(text = "TopBar title ") },
        )
    }
}
