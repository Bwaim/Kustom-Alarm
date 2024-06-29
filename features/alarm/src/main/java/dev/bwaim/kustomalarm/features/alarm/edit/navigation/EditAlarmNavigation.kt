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

package dev.bwaim.kustomalarm.features.alarm.edit.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import dev.bwaim.kustomalarm.features.alarm.edit.EditAlarmRoute
import dev.bwaim.kustomalarm.features.alarm.sound.navigation.SELECTED_URI_ARG
import dev.bwaim.kustomalarm.navigation.composable
import dev.bwaim.kustomalarm.navigation.state.MenuAppArguments
import kotlinx.serialization.Serializable

public const val NO_ALARM: Int = -1

private const val EDIT_ALARM_SCREEN_NAME: String = "Edit alarm"

@Serializable
internal class EditAlarmRoute(
    val alarmId: Int,
    val duplicate: Boolean = false,
    override val allowToOpenDrawer: Boolean = false,
) : MenuAppArguments()

public fun NavController.navigateToEditAlarmScreen(
    alarmId: Int = NO_ALARM,
    duplicate: Boolean = false,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        route = EditAlarmRoute(alarmId = alarmId, duplicate = duplicate),
        navOptions = navOptions,
    )
}

public fun NavGraphBuilder.editAlarmScreen(
    close: () -> Unit,
    onSoundSelectionClick: (String) -> Unit,
    previewAlarm: (Int, String) -> Unit,
) {
    composable<EditAlarmRoute>(
        screenName = EDIT_ALARM_SCREEN_NAME,
    ) { backStackEntry ->
        val uriSelected: String? by backStackEntry.savedStateHandle
            .getStateFlow(
                SELECTED_URI_ARG,
                null,
            ).collectAsStateWithLifecycle()

        EditAlarmRoute(
            selectedUri = uriSelected,
            cleanBackstack = {
                backStackEntry.savedStateHandle.remove<String>(SELECTED_URI_ARG)
            },
            close = close,
            onSoundSelectionClick = onSoundSelectionClick,
            previewAlarm = previewAlarm,
        )
    }
}
