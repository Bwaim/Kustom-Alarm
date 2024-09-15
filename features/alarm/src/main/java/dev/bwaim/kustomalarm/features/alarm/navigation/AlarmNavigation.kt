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

package dev.bwaim.kustomalarm.features.alarm.navigation

import androidx.navigation.NavGraphBuilder
import dev.bwaim.kustomalarm.features.alarm.AlarmRoute
import dev.bwaim.kustomalarm.navigation.composable
import dev.bwaim.kustomalarm.navigation.state.MenuAppArguments
import kotlinx.serialization.Serializable

private const val ALARM_SCREEN_NAME: String = "Alarms' list"

@Serializable
public class AlarmRoute : MenuAppArguments()

public fun NavGraphBuilder.alarmScreen(
    openDrawer: () -> Unit,
    addAlarm: (Int, Boolean) -> Unit,
    previewAlarm: (Int) -> Unit,
) {
    composable<AlarmRoute>(
        screenName = ALARM_SCREEN_NAME,
    ) {
        AlarmRoute(
            openDrawer = openDrawer,
            addAlarm = addAlarm,
            previewAlarm = previewAlarm,
        )
    }
}
