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

package dev.bwaim.kustomalarm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bwaim.kustomalarm.features.alarm.navigation.alarmScreen
import dev.bwaim.kustomalarm.features.settings.navigation.settingsScreen

@Composable
internal fun KAlarmNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startRoute: String,
    openDrawer: () -> Unit,
    navigateUp: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startRoute,
        modifier = modifier,
    ) {
        alarmScreen(
            openDrawer = openDrawer,
        )

        settingsScreen(
            onClose = navigateUp,
        )
    }
}