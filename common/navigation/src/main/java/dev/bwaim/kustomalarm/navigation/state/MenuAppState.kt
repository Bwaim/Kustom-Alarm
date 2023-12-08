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

package dev.bwaim.kustomalarm.navigation.state

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

private const val NAVIGATION_DRAWER_ITEM_ID = "navigationDrawerItemId"
private const val ALLOW_TO_OPEN_DRAWER = "allowToOpenDrawer"

public data class MenuAppState(
    val selectedNavigationDrawerId: String? = null,
    val allowToOpenDrawer: Boolean = true,
) {
    internal fun generateAppParameters() =
        listOfNotNull(
            selectedNavigationDrawerId?.let { NAVIGATION_DRAWER_ITEM_ID to it },
            ALLOW_TO_OPEN_DRAWER to allowToOpenDrawer,
        )

    internal companion object {
        fun getAppParameters(): List<NamedNavArgument> =
            listOf(
                navArgument(NAVIGATION_DRAWER_ITEM_ID) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(ALLOW_TO_OPEN_DRAWER) {
                    type = NavType.BoolType
                    nullable = false
                    defaultValue = true
                },
            )
    }
}

public val LocalMenuAppStateSetter: ProvidableCompositionLocal<(MenuAppState) -> Unit> =
    staticCompositionLocalOf {
        {}
    }

internal fun Bundle.toMenuAppState(): MenuAppState {
    val navigationDrawerId = Uri.decode(getString(NAVIGATION_DRAWER_ITEM_ID))
    val allowToOpenDrawer = getBoolean(ALLOW_TO_OPEN_DRAWER, true)
    return MenuAppState(
        selectedNavigationDrawerId = navigationDrawerId,
        allowToOpenDrawer = allowToOpenDrawer,
    )
}
