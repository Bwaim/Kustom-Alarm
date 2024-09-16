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

package dev.bwaim.kustomalarm.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.bwaim.kustomalarm.core.SaveEvents
import dev.bwaim.kustomalarm.core.SaveEvents.Failure
import dev.bwaim.kustomalarm.core.SaveEvents.Success
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
public fun SaveEventsEffect(
    eventFlow: SharedFlow<SaveEvents>,
    successAction: (String) -> Unit,
    failureAction: (String) -> Unit,
) {
    LaunchedEffect(successAction, failureAction) {
        eventFlow
            .onEach { event ->
                when (event) {
                    is Success -> successAction(event.text)
                    is Failure -> failureAction(event.text)
                }
            }.launchIn(this)
    }
}
