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

package dev.bwaim.kustomalarm.analytics

import dev.bwaim.kustomalarm.analytics.model.KaEvent
import dev.bwaim.kustomalarm.core.IODispatcher
import dev.bwaim.kustomalarm.core.executeCatching
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

public class AnalyticsService
    @Inject
    constructor(
        @IODispatcher private val ioDispatcher: CoroutineDispatcher,
        private val analyticsRepository: AnalyticsRepository,
    ) {
        public suspend fun logScreenView(
            screenName: String,
            screenClass: String,
        ) {
            executeCatching(ioDispatcher) {
                analyticsRepository.logScreenView(screenName = screenName, screenClass = screenClass)
            }
        }

        public suspend fun logEvent(event: KaEvent) {
            executeCatching(ioDispatcher) {
                analyticsRepository.logEvent(event)
            }
        }
    }
