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

@file:OptIn(ExperimentalLayoutApi::class)

package dev.bwaim.kustomalarm

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bwaim.kustomalarm.features.alarm.navigation.ALARM_NAVIGATION_ROUTE
import dev.bwaim.kustomalarm.navigation.KAlarmNavHost
import dev.bwaim.kustomalarm.state.KAlarmAppState
import dev.bwaim.kustomalarm.state.rememberKAlarmAppState

@Composable
internal fun KAlarmApp(
    kAlarmAppState: KAlarmAppState = rememberKAlarmAppState(),
) {
    Scaffold { contentPadding ->
        AnimatedContent(
            label = "Animated NxNavHost",
            targetState = contentPadding,
        ) { targetContentPadding ->
            KAlarmNavHost(
                navController = kAlarmAppState.navController,
                startRoute = ALARM_NAVIGATION_ROUTE,
                modifier = Modifier
                    .padding(targetContentPadding)
                    .consumeWindowInsets(targetContentPadding),
            )
        }
    }
}
