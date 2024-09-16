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

package dev.bwaim.kustomalarm.settings

import app.cash.turbine.test
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import dev.bwaim.kustomalarm.testing.repository.TestAppStateRepository
import dev.bwaim.kustomalarm.testing.repository.TestThemeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class SettingsServiceTest {
    private lateinit var subject: SettingsService

    @Before
    fun setUp() {
        subject =
            SettingsService(
                ioDispatcher = UnconfinedTestDispatcher(),
                themeRepository = TestThemeRepository(),
                appStateRepository = TestAppStateRepository(),
            )
    }

    @Test
    fun themeService_observe_themeChanges() =
        runTest {
            subject.observeTheme().test {
                Assert.assertEquals(Theme.DARK, awaitItem())
                subject.setTheme(Theme.LIGHT)
                Assert.assertEquals(Theme.LIGHT, awaitItem())
                cancel()
            }
        }

    @Test
    fun themeService_getThemes_returnAllThemes() {
        val expectedResult = listOf(Theme.LIGHT, Theme.DARK, Theme.SYSTEM, Theme.BATTERY_SAVER)

        val result = subject.getThemes()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun localeService_getLocales_returnAllLocales() {
        val expectedResult =
            listOf(
                Locale("es"),
                Locale.ENGLISH,
                Locale.FRENCH,
            )

        val result = subject.getLocales()

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun appStateService_observe_changes() =
        runTest {
            val defaultRingingAlarm = 0
            val ringingAlarmSet = 3

            subject.observeRingingAlarm().test {
                Assert.assertEquals(defaultRingingAlarm, awaitItem())
                subject.setRingingAlarm(ringingAlarmSet)
                Assert.assertEquals(ringingAlarmSet, awaitItem())
                cancel()
            }
        }
}
