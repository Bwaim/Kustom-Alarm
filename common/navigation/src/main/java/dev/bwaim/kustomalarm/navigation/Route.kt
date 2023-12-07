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

import android.net.Uri
import android.os.Bundle
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.bwaim.kustomalarm.navigation.state.LocalMenuAppStateSetter
import dev.bwaim.kustomalarm.navigation.state.MenuAppState
import kotlinx.collections.immutable.ImmutableList

/**
 * For a route like that example_route/{arg1}/{arg2}?arg3={arg3},arg4={arg4} baseRoutePattern =
 * example_route/{arg1}/{arg2} optionalArguments = listOf("arg3", "arg4")
 */
public interface Route {
    public val baseRoutePattern: String
    public val mandatoryArguments: ImmutableList<NamedNavArgument>
    public val optionalArguments: ImmutableList<NamedNavArgument>

    public val menuAppState: MenuAppState

    public val route: String
        get() = "$baseRoutePattern${addOptionalParameters()}"

    public fun buildRoute(
        params: List<Pair<String, Any>> = emptyList(),
        optionalParams: List<Pair<String, Any>> = emptyList(),
    ): String {
        var finalRoute = baseRoutePattern
        params.forEach { (argName, value) ->
            finalRoute = finalRoute.replace("{$argName}", value.encodedValue())
        }

        finalRoute +=
            (optionalParams + generateAppParameters()).joinToString(prefix = "?", separator = "&") {
                "${it.first}=${it.second.encodedValue()}"
            }

        return finalRoute
    }

    context(NavGraphBuilder)
    public fun composable(
        deepLinks: List<NavDeepLink> = emptyList(),
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
            null,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
            null,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
            enterTransition,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
            exitTransition,
        content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
    ) {
        composable(
            route = this@Route.route,
            arguments = mandatoryArguments + optionalArguments + getAppParameters(),
            deepLinks = deepLinks,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            content = {
                it.arguments?.let { args ->
                    LocalMenuAppStateSetter.current.invoke(args.toMenuAppState())
                }

                content(it)
            },
        )
    }

    private fun getAppParameters(): List<NamedNavArgument> =
        listOf(
            navArgument(NAVIGATION_DRAWER_ITEM_ID) {
                type = NavType.StringType
                nullable = true
            },
        )

    private fun generateAppParameters() =
        listOfNotNull(
            menuAppState.selectedNavigationDrawerId?.let { NAVIGATION_DRAWER_ITEM_ID to it },
        )

    private fun addOptionalParameters(): String =
        (optionalArguments + getAppParameters()).joinToString(separator = "&", prefix = "?") {
            "${it.name}={${it.name}}"
        }

    private fun Any.encodedValue(): String =
        when (this) {
            is String -> Uri.encode(this)
            else -> this.toString()
        }
}

private const val NAVIGATION_DRAWER_ITEM_ID = "navigationDrawerItemId"

private fun Bundle.toMenuAppState(): MenuAppState {
    val navigationDrawerId = Uri.decode(getString(NAVIGATION_DRAWER_ITEM_ID))
    return MenuAppState(
        selectedNavigationDrawerId = navigationDrawerId,
    )
}
