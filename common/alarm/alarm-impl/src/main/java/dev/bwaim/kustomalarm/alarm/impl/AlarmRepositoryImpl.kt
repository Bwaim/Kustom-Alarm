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

import dev.bwaim.kustomalarm.alarm.AlarmRepository
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.alarm.domain.WeekDay
import dev.bwaim.kustomalarm.database.alarm.AlarmDao
import dev.bwaim.kustomalarm.database.alarm.AlarmEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class AlarmRepositoryImpl
    @Inject constructor(
        private val alarmDao: AlarmDao,
    ) : AlarmRepository {
        override fun observeAlarms(): Flow<List<Alarm>> {
            return alarmDao.observeAlarms().map { it.map(AlarmEntity::toDomain) }
        }

        override suspend fun getAlarm(alarmId: Int): Alarm? {
            return alarmDao.getAlarm(id = alarmId)?.toDomain()
        }

        override suspend fun saveAlarm(alarm: Alarm) {
            alarmDao.upsertAlarm(alarm.toEntity())
        }

        override suspend fun deleteAlarm(alarmId: Int) {
            alarmDao.deleteAlarm(alarmId = alarmId)
        }
    }

private fun AlarmEntity.toDomain(): Alarm =
    Alarm(
        id = id,
        name = name,
        time = time,
        weekDays = weekDays.toWeekDays(),
    )

private fun Int.toWeekDays(): List<WeekDay> =
    WeekDay.entries.mapNotNull { day ->
        if (this and day.value != 0) {
            day
        } else {
            null
        }
    }

private fun Alarm.toEntity(): AlarmEntity =
    AlarmEntity(
        id = id,
        name = name,
        time = time,
        weekDays = weekDays.toInt(),
    )

private fun List<WeekDay>.toInt(): Int = this.sumOf { it.value }
