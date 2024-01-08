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

package dev.bwaim.kustomalarm.database.alarm

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.bwaim.kustomalarm.database.KustomAlarmRoomDatabase
import dev.bwaim.kustomalarm.database.KustomAlarmTypeConverters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalTime

internal class AlarmDaoTest {
    private lateinit var alarmDao: AlarmDao
    private lateinit var db: KustomAlarmRoomDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room.inMemoryDatabaseBuilder(
                context,
                KustomAlarmRoomDatabase::class.java,
            )
                .addTypeConverter(KustomAlarmTypeConverters())
                .build()
        alarmDao = db.alarmDao()
    }

    @Test
    fun alarmDao_observes_alarms() =
        runTest {
            val alarms =
                listOf(
                    testAlarm(name = "alarm1", uri = "uri1"),
                    testAlarm(name = "alarm2", uri = "uri2"),
                )

            alarms.forEach { alarmDao.upsertAlarm(it) }

            var savedAlarms = alarmDao.observeAlarms().first()

            Assert.assertEquals(
                listOf("alarm1", "alarm2"),
                savedAlarms.map { it.name },
            )

            alarmDao.deleteAlarm(savedAlarms.first().id)

            savedAlarms = alarmDao.observeAlarms().first()

            Assert.assertEquals(
                listOf("alarm2"),
                savedAlarms.map { it.name },
            )
        }

    @Test
    fun alarmDao_insert_right_data() =
        runTest {
            val alarms =
                listOf(
                    testAlarm(
                        name = "alarm1",
                        time = LocalTime.of(8, 59),
                        weekDays = setOf(DayOfWeek.TUESDAY),
                        uri = "uri1",
                    ),
                    testAlarm(name = "alarm2", uri = "uri2"),
                )

            alarms.forEach { alarmDao.upsertAlarm(it) }

            val alarmRetrieved = alarmDao.getAlarm(id = 1)

            Assert.assertEquals(
                AlarmEntity(
                    id = 1,
                    name = "alarm1",
                    time = LocalTime.of(8, 59),
                    weekDays = "2",
                    isOnce = false,
                    isActivated = true,
                    uri = "uri1",
                ),
                alarmRetrieved,
            )
        }

    @Test
    fun alarmDao_update_template() =
        runTest {
            val firstTemplate =
                testAlarmTemplate(
                    name = "template1",
                    time = LocalTime.of(7, 0),
                    setOf(DayOfWeek.TUESDAY),
                    uri = "uriTemplate1",
                )
            val secondTemplate =
                testAlarmTemplate(
                    name = null,
                    time = LocalTime.of(10, 0),
                    setOf(DayOfWeek.MONDAY),
                    uri = "uriTemplate2",
                )

            alarmDao.upsertAlarmTemplate(firstTemplate)
            val res1 = alarmDao.getAlarmTemplate()

            Assert.assertEquals(
                firstTemplate,
                res1,
            )

            alarmDao.upsertAlarmTemplate(secondTemplate)
            val res2 = alarmDao.getAlarmTemplate()

            Assert.assertEquals(
                secondTemplate,
                res2,
            )
        }
}

private fun testAlarm(
    id: Int = 0,
    name: String,
    time: LocalTime = LocalTime.of(10, 30),
    weekDays: Set<DayOfWeek> = setOf(),
    uri: String,
) = AlarmEntity(
    id = id,
    name = name,
    time = time,
    weekDays = weekDays.joinToString { it.value.toString() },
    isOnce = false,
    isActivated = true,
    uri = uri,
)

private fun testAlarmTemplate(
    name: String?,
    time: LocalTime = LocalTime.of(10, 30),
    weekDays: Set<DayOfWeek> = setOf(),
    uri: String,
) = AlarmTemplateEntity(
    name = name,
    time = time,
    weekDays = weekDays.joinToString { it.value.toString() },
    uri = uri,
)
