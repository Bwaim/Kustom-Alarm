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

import androidx.datastore.core.CorruptionException
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal class AppStatePreferencesSerializerTest {
    private val appStatePreferencesSerializer = AppStatePreferencesSerializer()

    @Test
    fun defaultAppStatePreferences_isEmpty() {
        Assert.assertEquals(
            appStatePreferencesSerializer.defaultValue,
            appStatePreferences {
                // Default value
            },
        )
    }

    @Test
    fun writingAndReadingAppStatePreferences_outputsCorrectValue() =
        runTest {
            val expectedAppStatePreferences = appStatePreferences { ringingAlarm = -1 }

            val outputStream = ByteArrayOutputStream()

            expectedAppStatePreferences.writeTo(outputStream)

            val inputStream = ByteArrayInputStream(outputStream.toByteArray())

            val actualAppStatePreferences = appStatePreferencesSerializer.readFrom(inputStream)

            Assert.assertEquals(
                expectedAppStatePreferences,
                actualAppStatePreferences,
            )
        }

    @Test(expected = CorruptionException::class)
    fun readingInvalidAppStatePreferences_throwsCorruptionException() =
        runTest {
            appStatePreferencesSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
        }
}
