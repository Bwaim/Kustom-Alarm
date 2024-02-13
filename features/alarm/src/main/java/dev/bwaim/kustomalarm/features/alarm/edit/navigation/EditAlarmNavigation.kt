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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.bwaim.kustomalarm.features.alarm.edit.EditAlarmRoute
import dev.bwaim.kustomalarm.features.alarm.sound.navigation.SELECTED_URI_ARG
import dev.bwaim.kustomalarm.navigation.Route
import dev.bwaim.kustomalarm.navigation.state.MenuAppState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

public const val NO_ALARM: Int = -1
public const val ALARM_ID_ARG: String = "ALARM_ID_ARG"
public const val DUPLICATE_ARG: String = "DUPLICATE_ARG"

public const val EDIT_ALARM_NAVIGATION_ROUTE: String = "alarm/edit/{$ALARM_ID_ARG}"
private const val EDIT_ALARM_SCREEN_NAME: String = "Edit alarm"
private const val EDIT_ALARM_SCREEN_CLASS: String = "EditAlarmRoute"

internal class EditAlarmArgs(val alarmId: Int, val duplicate: Boolean) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            alarmId = (checkNotNull(savedStateHandle[ALARM_ID_ARG]) as Int),
            duplicate = (checkNotNull(savedStateHandle[DUPLICATE_ARG]) as Boolean),
        )
}

private object EditAlarmRoute : Route {
    override val baseRoutePattern: String = EDIT_ALARM_NAVIGATION_ROUTE
    override val mandatoryArguments: PersistentList<NamedNavArgument> =
        persistentListOf(
            navArgument(ALARM_ID_ARG) { type = NavType.IntType },
        )
    override val optionalArguments: PersistentList<NamedNavArgument> =
        persistentListOf(
            navArgument(DUPLICATE_ARG) {
                type = NavType.BoolType
                defaultValue = false
            },
        )

    override val menuAppState: MenuAppState = MenuAppState(allowToOpenDrawer = false)

    override val screenName: String = EDIT_ALARM_SCREEN_NAME
    override val screenClass: String = EDIT_ALARM_SCREEN_CLASS
}

public fun NavController.navigateToEditAlarmScreen(
    alarmId: Int = NO_ALARM,
    duplicate: Boolean? = null,
    navOptions: NavOptions? = null,
) {
    val params =
        persistentListOf(
            ALARM_ID_ARG to alarmId,
        )
    val optionalParams =
        listOfNotNull(
            duplicate?.let { DUPLICATE_ARG to duplicate },
        )

    this.navigate(
        EditAlarmRoute.buildRoute(params = params, optionalParams = optionalParams),
        navOptions,
    )
}

public fun NavGraphBuilder.editAlarmScreen(
    close: () -> Unit,
    onSoundSelectionClick: (String) -> Unit,
    openRingActivity: (Int) -> Unit,
) {
    EditAlarmRoute.composable { backStackEntry ->
        val uriSelected: String? by backStackEntry.savedStateHandle.getStateFlow(
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
            openRingActivity = openRingActivity,
        )
    }
}
