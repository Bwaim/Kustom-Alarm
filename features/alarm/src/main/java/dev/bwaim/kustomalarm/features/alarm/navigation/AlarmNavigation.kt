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

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import dev.bwaim.kustomalarm.features.alarm.AlarmRoute
import dev.bwaim.kustomalarm.navigation.Route

public const val ALARM_NAVIGATION_ROUTE: String = "alarm"

public object AlarmRoute : Route {
    override val baseRoutePattern: String = ALARM_NAVIGATION_ROUTE
    override val mandatoryArguments: List<NamedNavArgument> = emptyList()
    override val optionalArguments: List<NamedNavArgument> = emptyList()
}

public fun NavGraphBuilder.alarmScreen(
    openDrawer: () -> Unit,
) {
    AlarmRoute.composable {
        AlarmRoute(
            openDrawer = openDrawer,
        )
    }
}
