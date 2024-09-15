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

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import dev.bwaim.kustomalarm.features.alarm.sound.SoundSelectionRoute
import dev.bwaim.kustomalarm.navigation.composable
import dev.bwaim.kustomalarm.navigation.domain.BackResultArgument
import dev.bwaim.kustomalarm.navigation.state.MenuAppArguments
import kotlinx.serialization.Serializable

private const val SOUND_SELECTION_SCREEN_NAME: String = "Sound selection"

public const val SELECTED_URI_ARG: String = "SELECTED_URI_ARG"

@Serializable
internal class SoundSelectionRoute(
    val uri: String = "",
    override val allowToOpenDrawer: Boolean = false,
) : MenuAppArguments()

public fun NavController.navigateToSoundSelectionScreen(
    uri: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(route = SoundSelectionRoute(uri = uri), navOptions = navOptions)
}

public fun NavGraphBuilder.soundSelectionScreen(close: (backData: List<BackResultArgument<Any>>) -> Unit) {
    composable<SoundSelectionRoute>(
        screenName = SOUND_SELECTION_SCREEN_NAME,
    ) {
        SoundSelectionRoute(
            close = close,
        )
    }
}
