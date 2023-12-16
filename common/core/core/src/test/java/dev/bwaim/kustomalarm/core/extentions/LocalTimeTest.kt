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

package dev.bwaim.kustomalarm.core.extentions

import org.junit.Assert
import org.junit.Test
import java.time.LocalTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

internal class LocalTimeTest {
    @Test
    fun localTime_duration_to_time_after() {
        val time1 = LocalTime.of(17, 57)
        val time2 = LocalTime.of(18, 0)
        val expectedDuration = 3.minutes

        val duration = time1.durationTo(time2)

        Assert.assertEquals(
            expectedDuration,
            duration,
        )
    }

    @Test
    fun localTime_duration_to_time_before() {
        val time1 = LocalTime.of(10, 0)
        val time2 = LocalTime.of(9, 35)
        val expectedDuration = 23.hours + 35.minutes

        val duration = time1.durationTo(time2)

        Assert.assertEquals(
            expectedDuration,
            duration,
        )
    }
}
