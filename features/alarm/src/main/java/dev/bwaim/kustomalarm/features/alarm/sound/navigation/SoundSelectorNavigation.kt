/*
 * Copyright 2024 Dev Bwaim team
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

package dev.bwaim.kustomalarm.features.alarm.sound.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import dev.bwaim.kustomalarm.features.alarm.sound.SoundSelectionRoute
import dev.bwaim.kustomalarm.navigation.Route
import dev.bwaim.kustomalarm.navigation.state.MenuAppState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

public const val SOUND_SELECTION_NAVIGATION_ROUTE: String = "alarm/edit/sound_selection"
private const val SOUND_SELECTION_SCREEN_NAME: String = "Sound selection"
private const val SOUND_SELECTION_SCREEN_CLASS: String = "SoundSelectionRoute"

private object SoundSelectionRoute : Route {
    override val baseRoutePattern: String = SOUND_SELECTION_NAVIGATION_ROUTE
    override val mandatoryArguments: PersistentList<NamedNavArgument> =
        persistentListOf()
    override val optionalArguments: PersistentList<NamedNavArgument> =
        persistentListOf()

    override val menuAppState: MenuAppState = MenuAppState(allowToOpenDrawer = false)

    override val screenName: String = SOUND_SELECTION_SCREEN_NAME
    override val screenClass: String = SOUND_SELECTION_SCREEN_CLASS
}

public fun NavController.navigateToSoundSelectionScreen(navOptions: NavOptions? = null) {
    this.navigate(SoundSelectionRoute.buildRoute(), navOptions)
}

public fun NavGraphBuilder.soundSelectionScreen(close: () -> Unit) {
    SoundSelectionRoute.composable {
        SoundSelectionRoute(
            close = close,
        )
    }
}
