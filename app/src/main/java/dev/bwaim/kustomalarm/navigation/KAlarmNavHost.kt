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

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.bwaim.kustomalarm.features.alarm.edit.navigation.editAlarmScreen
import dev.bwaim.kustomalarm.features.alarm.edit.navigation.navigateToEditAlarmScreen
import dev.bwaim.kustomalarm.features.alarm.navigation.alarmScreen
import dev.bwaim.kustomalarm.features.alarm.ring.RingingAlarmService
import dev.bwaim.kustomalarm.features.alarm.sound.navigation.navigateToSoundSelectionScreen
import dev.bwaim.kustomalarm.features.alarm.sound.navigation.soundSelectionScreen
import dev.bwaim.kustomalarm.features.settings.navigation.settingsScreen
import dev.bwaim.kustomalarm.navigation.domain.BackResultArgument

@Composable
internal fun KAlarmNavHost(
    navController: NavHostController,
    startRoute: String,
    openDrawer: () -> Unit,
    navigateUp: () -> Unit,
    backWithResult: (backData: List<BackResultArgument<Any>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val previewAlarm =
        remember(context) {
            { alarmId: Int, uri: String? ->
                val intent =
                    RingingAlarmService.createIntent(
                        context = context,
                        alarmId = alarmId,
                        alarmUri = uri,
                    )
                context.startService(intent)
                Unit
            }
        }

    NavHost(
        navController = navController,
        startDestination = startRoute,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        alarmScreen(
            openDrawer = openDrawer,
            addAlarm = navController::navigateToEditAlarmScreen,
            previewAlarm = { previewAlarm(it, null) },
        )

        settingsScreen(
            onClose = navigateUp,
        )

        editAlarmScreen(
            close = navigateUp,
            onSoundSelectionClick = navController::navigateToSoundSelectionScreen,
            previewAlarm = previewAlarm,
        )

        soundSelectionScreen(
            close = backWithResult,
        )
    }
}
