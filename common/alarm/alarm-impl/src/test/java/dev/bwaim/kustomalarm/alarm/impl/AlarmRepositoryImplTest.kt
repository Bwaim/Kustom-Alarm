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

package dev.bwaim.kustomalarm.alarm.impl

import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.alarm.domain.AlarmTemplate
import dev.bwaim.kustomalarm.alarm.impl.testdoubles.TestAlarmDao
import dev.bwaim.kustomalarm.testing.utils.TestRingtoneUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalTime

internal class AlarmRepositoryImplTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: AlarmRepositoryImpl

    private lateinit var alarmDao: TestAlarmDao

    @Before
    fun setup() {
        alarmDao = TestAlarmDao()

        subject =
            AlarmRepositoryImpl(
                alarmDao = alarmDao,
                ringtoneUtils = TestRingtoneUtils(),
            )
    }

    @Test
    fun alarmRepository_is_backed_by_alarm_dao() =
        testScope.runTest {
            Assert.assertTrue(
                subject.observeAlarms().first().isEmpty(),
            )

            val alarm1 =
                Alarm(name = "alarm1", time = LocalTime.of(10, 45), weekDays = setOf(DayOfWeek.MONDAY), uri = "uri1")
            val alarm2 =
                Alarm(name = "alarm2", time = LocalTime.of(8, 15), weekDays = setOf(DayOfWeek.SATURDAY), uri = "uri2")

            subject.saveAlarm(alarm1)
            subject.saveAlarm(alarm2)

            Assert.assertEquals(
                listOf(alarm2.copy(id = 2), alarm1.copy(id = 1)),
                subject.observeAlarms().first(),
            )

            Assert.assertEquals(
                alarm2.copy(id = 2),
                subject.getAlarm(alarmId = 2),
            )

            subject.deleteAlarm(alarmId = 1)

            Assert.assertEquals(
                listOf(alarm2.copy(id = 2)),
                subject.observeAlarms().first(),
            )
        }

    @Test
    fun alarmRepository_get_template() =
        testScope.runTest {
            val defaultTemplate =
                AlarmTemplate(
                    name = null,
                    time = LocalTime.of(7, 0),
                    weekDays = emptySet(),
                    uri = "defaultUri",
                )

            val first = subject.getTemplate()

            Assert.assertEquals(
                defaultTemplate,
                first,
            )

            val secondTemplate = defaultTemplate.copy(name = "template")
            subject.saveTemplate(secondTemplate)
            val second = subject.getTemplate()

            Assert.assertEquals(
                secondTemplate,
                second,
            )
        }
}
