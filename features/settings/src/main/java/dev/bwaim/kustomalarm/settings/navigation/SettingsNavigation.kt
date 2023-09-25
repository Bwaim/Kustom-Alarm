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

package dev.bwaim.kustomalarm.settings.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.navigation.NavigationDrawerItem
import dev.bwaim.kustomalarm.navigation.Route
import dev.bwaim.kustomalarm.settings.SettingsRoute
import javax.inject.Inject

private const val SETTINGS_NAVIGATION_ROUTE: String = "settings"

public object SettingsRoute : Route {
    override val baseRoutePattern: String = SETTINGS_NAVIGATION_ROUTE
    override val mandatoryArguments: List<NamedNavArgument> = emptyList()
    override val optionalArguments: List<NamedNavArgument> = emptyList()
}

public class SettingsNavigationDrawerItem @Inject constructor() : NavigationDrawerItem {
    override val labelRes: Int = string.settings_screen_label
    override val icon: ImageVector = Icons.Filled.Settings
    override val action: (NavController) -> Unit =
        { navController -> navController.navigateToSettings() }
}

public fun NavController.navigateToSettings(
    navOptions: NavOptions? = buildSettingsNavOptions(),
) {
    this.navigate(
        SettingsRoute.buildRoute(params = emptyList(), optionalParams = emptyList()),
        navOptions,
    )
}

public fun NavGraphBuilder.settingsScreen(
    onClose: () -> Unit,
) {
    SettingsRoute.composable {
        SettingsRoute(
            onClose = onClose,
        )
    }
}

private fun buildSettingsNavOptions(): NavOptions = NavOptions.Builder()
    .setLaunchSingleTop(true)
    .build()
