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
import dev.bwaim.kustomalarm.alarm.domain.AlarmTemplate
import dev.bwaim.kustomalarm.core.value
import dev.bwaim.kustomalarm.testing.repository.TestAlarmRepository
import dev.bwaim.kustomalarm.testing.repository.defaultTemplate
import dev.bwaim.kustomalarm.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

internal class AlarmServiceTest {
    private lateinit var subject: AlarmService

    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        subject =
            AlarmService(
                ioDispatcher = UnconfinedTestDispatcher(),
                alarmRepository = TestAlarmRepository(),
            )
    }

    @Test
    fun alarmService_observe_alarms() =
        runTest {
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

    @Test
    fun alarmService_insert_one_time_alarm() =
        runTest {
            val now = LocalTime.now()
            val adjustedNow = LocalTime.of(now.hour, now.minute)
            val today = LocalDate.now().dayOfWeek

            val emptyAlarmForNextDay =
                Alarm(
                    id = 1,
                    name = "",
                    time = adjustedNow.minusHours(1),
                    weekDays = setOf(),
                )
            val emptyAlarmForThisDay =
                Alarm(
                    id = 2,
                    name = null,
                    time = adjustedNow.plusHours(1),
                    weekDays = setOf(),
                )

            val expectedAlarmNextDay =
                emptyAlarmForNextDay.copy(
                    name = "",
                    weekDays = setOf(today.plus(1)),
                    isOnce = true,
                )
            val expectedAlarmThisDay =
                emptyAlarmForThisDay.copy(
                    name = null,
                    weekDays = setOf(today),
                    isOnce = true,
                )

            subject.saveAlarm(emptyAlarmForNextDay)
            subject.saveAlarm(emptyAlarmForThisDay)

            val resAlarmNextDay = subject.getAlarm(alarmId = emptyAlarmForNextDay.id).value
            Assert.assertEquals(
                expectedAlarmNextDay,
                resAlarmNextDay,
            )

            val resAlarmThisDay = subject.getAlarm(alarmId = emptyAlarmForThisDay.id).value
            Assert.assertEquals(
                expectedAlarmThisDay,
                resAlarmThisDay,
            )
        }

    @Test
    fun alarmService_get_default() =
        runTest {
            val expectedAlarm = defaultTemplate.toAlarm()

            val resAlarm = subject.getDefaultAlarm()
            Assert.assertEquals(
                expectedAlarm,
                resAlarm.value,
            )

            val template =
                AlarmTemplate(
                    name = "template",
                    time = LocalTime.of(16, 54),
                    weekDays = emptySet(),
                )
            subject.saveTemplate(template)
            val resAlarm2 = subject.getDefaultAlarm()
            Assert.assertEquals(
                template.toAlarm(),
                resAlarm2.value,
            )
        }
}

private val testAlarms =
    listOf(
        Alarm(
            id = 1,
            name = "alarm1",
            time = LocalTime.of(8, 0),
            weekDays = setOf(DayOfWeek.MONDAY),
        ),
        Alarm(
            id = 2,
            name = "alarm2",
            time = LocalTime.of(8, 10),
            weekDays = setOf(DayOfWeek.WEDNESDAY),
        ),
        Alarm(
            id = 3,
            name = "alarm3",
            time = LocalTime.of(9, 0),
            weekDays = setOf(DayOfWeek.SUNDAY),
        ),
    )
