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

package dev.bwaim.kustomalarm

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.bwaim.kustomalarm.compose.KaBackground
import dev.bwaim.kustomalarm.features.alarm.navigation.ALARM_NAVIGATION_ROUTE
import dev.bwaim.kustomalarm.features.alarm.navigation.AlarmRoute
import dev.bwaim.kustomalarm.navigation.KAlarmNavHost
import dev.bwaim.kustomalarm.navigation.KaNavigationDrawer
import dev.bwaim.kustomalarm.navigation.NavigationDrawerItem
import dev.bwaim.kustomalarm.navigation.state.LocalMenuAppStateSetter
import dev.bwaim.kustomalarm.navigation.state.MenuAppState
import dev.bwaim.kustomalarm.state.KAlarmAppState
import dev.bwaim.kustomalarm.state.rememberKAlarmAppState
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch

@Composable
internal fun KAlarmApp(
    navigationDrawerItems: PersistentList<NavigationDrawerItem>,
    kAlarmAppState: KAlarmAppState = rememberKAlarmAppState(),
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var menuAppState by remember { mutableStateOf(MenuAppState()) }

    val openDrawer: () -> Unit = remember(scope) { { scope.launch { drawerState.open() } } }
    val navigateHome: () -> Unit =
        remember {
            { kAlarmAppState.navController.popBackStack(AlarmRoute.route, inclusive = false) }
        }
    val menuAppStateSetter: (MenuAppState) -> Unit =
        remember {
            { menuAppStateNew -> menuAppState = menuAppStateNew }
        }

    CompositionLocalProvider(LocalMenuAppStateSetter provides menuAppStateSetter) {
        KaNavigationDrawer(
            navigationDrawerItems = navigationDrawerItems,
            selectedNavigationDrawerId = menuAppState.selectedNavigationDrawerId,
            navController = kAlarmAppState.navController,
            drawerState = drawerState,
            scope = scope,
            navigateHome = navigateHome,
            allowToOpen = menuAppState.allowToOpenDrawer,
        ) {
            KaBackground {
                Scaffold(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { padding ->
                    Row(
                        Modifier.fillMaxSize()
                            .padding(padding)
                            .consumeWindowInsets(padding)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal,
                                ),
                            ),
                    ) {
                        KAlarmNavHost(
                            navController = kAlarmAppState.navController,
                            startRoute = ALARM_NAVIGATION_ROUTE,
                            openDrawer = openDrawer,
                            navigateUp = kAlarmAppState.navController::navigateUp,
                            backWithResult = kAlarmAppState::backWithResult,
                        )
                    }
                }
            }
        }
    }
}
