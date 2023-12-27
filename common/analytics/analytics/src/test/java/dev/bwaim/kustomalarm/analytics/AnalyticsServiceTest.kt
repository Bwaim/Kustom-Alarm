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

@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.bwaim.kustomalarm.analytics

import dev.bwaim.kustomalarm.testing.repository.TestAnalyticsRepository
import dev.bwaim.kustomalarm.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class AnalyticsServiceTest {
    private lateinit var subject: AnalyticsService
    private val testAnalyticsRepository = TestAnalyticsRepository()

    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        subject =
            AnalyticsService(
                ioDispatcher = UnconfinedTestDispatcher(),
                analyticsRepository = testAnalyticsRepository,
            )
    }

    @Test
    fun analytics_service_log_screen_view() =
        runTest {
            val screenName = "screenName"
            val screenClass = "screenClass"

            subject.logScreenView(screenName = screenName, screenClass = screenClass)

            val eventsSent = testAnalyticsRepository.events.value

            Assert.assertEquals(
                screenName,
                eventsSent?.first,
            )

            Assert.assertEquals(
                screenClass,
                eventsSent?.second,
            )
        }
}
