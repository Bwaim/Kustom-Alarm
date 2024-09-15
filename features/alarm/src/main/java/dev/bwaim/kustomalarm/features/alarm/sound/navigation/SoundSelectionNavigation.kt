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

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.bwaim.kustomalarm.features.alarm.sound.SoundSelectionRoute
import dev.bwaim.kustomalarm.navigation.Route
import dev.bwaim.kustomalarm.navigation.domain.BackResultArgument
import dev.bwaim.kustomalarm.navigation.state.MenuAppState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

public const val URI_ARG: String = "URI_ARG"

public const val SOUND_SELECTION_NAVIGATION_ROUTE: String = "alarm/edit/sound_selection"
private const val SOUND_SELECTION_SCREEN_NAME: String = "Sound selection"
private const val SOUND_SELECTION_SCREEN_CLASS: String = "SoundSelectionRoute"

public const val SELECTED_URI_ARG: String = "SELECTED_URI_ARG"

internal class SoundSelectionArgs(val uri: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            uri = Uri.decode(checkNotNull(savedStateHandle[URI_ARG]) as String),
        )
}

private object SoundSelectionRoute : Route {
    override val baseRoutePattern: String = SOUND_SELECTION_NAVIGATION_ROUTE
    override val mandatoryArguments: PersistentList<NamedNavArgument> =
        persistentListOf()
    override val optionalArguments: PersistentList<NamedNavArgument> =
        persistentListOf(
            navArgument(URI_ARG) {
                type = NavType.StringType
                defaultValue = ""
            },
        )

    override val menuAppState: MenuAppState = MenuAppState(allowToOpenDrawer = false)

    override val screenName: String = SOUND_SELECTION_SCREEN_NAME
    override val screenClass: String = SOUND_SELECTION_SCREEN_CLASS
}

public fun NavController.navigateToSoundSelectionScreen(
    uri: String,
    navOptions: NavOptions? = null,
) {
    val optionalParams =
        persistentListOf(
            URI_ARG to uri,
        )
    this.navigate(SoundSelectionRoute.buildRoute(optionalParams = optionalParams), navOptions)
}

public fun NavGraphBuilder.soundSelectionScreen(close: (backData: List<BackResultArgument<Any>>) -> Unit) {
    SoundSelectionRoute.composable(this) {
        SoundSelectionRoute(
            close = close,
        )
    }
}
