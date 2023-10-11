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

package dev.bwaim.kustomalarm.alarm

import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.alarm.domain.WeekDay.MONDAY
import dev.bwaim.kustomalarm.alarm.domain.WeekDay.SUNDAY
import dev.bwaim.kustomalarm.alarm.domain.WeekDay.WEDNESDAY
import dev.bwaim.kustomalarm.core.value
import dev.bwaim.kustomalarm.testing.repository.TestAlarmRepository
import dev.bwaim.kustomalarm.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalTime

internal class AlarmServiceTest {

    private lateinit var subject: AlarmService

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        subject = AlarmService(
            ioDispatcher = UnconfinedTestDispatcher(),
            alarmRepository = TestAlarmRepository(),
        )
    }

    @Test
    fun themeService_observe_alarms() = runTest {
        val alarms = subject.observeAlarms()

        testAlarms.forEach { alarm -> subject.saveAlarm(alarm) }

        Assert.assertEquals(
            testAlarms,
            alarms.first(),
        )

        subject.deleteAlarm(alarmId = 2)

        Assert.assertEquals(
            testAlarms.filterNot { it.id == 2 },
            alarms.first(),
        )

        val alarmRetrieved = subject.getAlarm(alarmId = 3)

        Assert.assertEquals(
            testAlarms[2],
            alarmRetrieved.value,
        )
    }
}

private val testAlarms = listOf(
    Alarm(id = 1, name = "alarm1", time = LocalTime.of(8, 0), weekDays = listOf(MONDAY)),
    Alarm(id = 2, name = "alarm2", time = LocalTime.of(8, 10), weekDays = listOf(WEDNESDAY)),
    Alarm(id = 3, name = "alarm3", time = LocalTime.of(9, 0), weekDays = listOf(SUNDAY)),
)
