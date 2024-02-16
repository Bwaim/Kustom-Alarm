/*
 * Copyright 2024 Dev Bwaim team
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

package dev.bwaim.kustomalarm.settings.impl.appstate

import app.cash.turbine.test
import dev.bwaim.kustomalarm.test.android.di.testAppStatePreferencesDataStore
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

internal class AppStateRepositoryImplTest {
    private lateinit var subject: AppStateRepositoryImpl

    @get:Rule val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setUp() {
        subject = AppStateRepositoryImpl(tmpFolder.testAppStatePreferencesDataStore())
    }

    // This test fails on Windows : https://github.com/android/nowinandroid/issues/98
    @Test
    fun ringingAlarm_observeChanges() = runTest {
        val defaultRingingAlarm = 0
        val alarmSet = 5

        subject.observeAppState().test {
            Assert.assertEquals(defaultRingingAlarm, awaitItem().ringingAlarm)
            subject.setRingingAlarm(alarmSet)
            Assert.assertEquals(alarmSet, awaitItem().ringingAlarm)
            cancel()
        }
    }
}
