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

package dev.bwaim.kustomalarm.features.settings.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import dev.bwaim.kustomalarm.features.settings.SettingsRoute
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.navigation.NavigationDrawerItem
import dev.bwaim.kustomalarm.navigation.composable
import dev.bwaim.kustomalarm.navigation.state.MenuAppArguments
import kotlinx.serialization.Serializable
import javax.inject.Inject

private const val NAVIGATION_DRAWER_SETTINGS_ID: String = "NavDrawerSettings"
private const val SETTINGS_SCREEN_NAME: String = "Settings"

@Serializable
public class SettingsRoute(
    override val selectedNavigationDrawerId: String = NAVIGATION_DRAWER_SETTINGS_ID,
) : MenuAppArguments()

public class SettingsNavigationDrawerItem @Inject constructor() : NavigationDrawerItem {
    override val id: String = NAVIGATION_DRAWER_SETTINGS_ID
    override val labelRes: Int = string.settings_screen_label
    override val icon: ImageVector = Icons.Filled.Settings
    override val action: (NavController) -> Unit = { navController ->
        navController.navigateToSettings()
    }
    override val testIdentifier: String = "settings_navigation"
}

public fun NavController.navigateToSettings(navOptions: NavOptions? = buildSettingsNavOptions()) {
    this.navigate(
        route = SettingsRoute(),
        navOptions = navOptions,
    )
}

public fun NavGraphBuilder.settingsScreen(onClose: () -> Unit) {
    composable<SettingsRoute>(
        screenName = SETTINGS_SCREEN_NAME,
    ) {
        SettingsRoute(
            onClose = onClose,
        )
    }
}

private fun buildSettingsNavOptions(): NavOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
