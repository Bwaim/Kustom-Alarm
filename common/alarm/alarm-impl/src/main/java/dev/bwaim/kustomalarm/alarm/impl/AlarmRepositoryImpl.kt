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
import dev.bwaim.kustomalarm.alarm.domain.AlarmTemplate
import dev.bwaim.kustomalarm.database.alarm.AlarmDao
import dev.bwaim.kustomalarm.database.alarm.AlarmEntity
import dev.bwaim.kustomalarm.database.alarm.AlarmTemplateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

internal class AlarmRepositoryImpl
    @Inject
    constructor(
        private val alarmDao: AlarmDao,
    ) : AlarmRepository {
        override fun observeAlarms(): Flow<List<Alarm>> {
            return alarmDao.observeAlarms()
                .map { it.map(AlarmEntity::toDomain).sortedBy { alarm -> alarm.time } }
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

        override suspend fun saveTemplate(alarmTemplate: AlarmTemplate) {
            alarmDao.upsertAlarmTemplate(alarmTemplate.toEntity())
        }

        override suspend fun getTemplate(): AlarmTemplate {
            return alarmDao.getAlarmTemplate()?.toDomain() ?: getDefaultTemplate()
        }

        private fun getDefaultTemplate(): AlarmTemplate =
            AlarmTemplate(
                name = null,
                time = LocalTime.of(7, 0),
                weekDays = emptySet(),
            )
    }

private fun AlarmEntity.toDomain(): Alarm =
    Alarm(
        id = id,
        name = name,
        time = time,
        weekDays = weekDays.toWeekDays(),
        isOnce = isOnce,
        isActivated = isActivated,
    )

private fun String.toWeekDays(): Set<DayOfWeek> =
    if (this.isBlank()) {
        emptySet()
    } else {
        this.split(",").map { DayOfWeek.of(it.trim().toInt()) }.toSet()
    }

private fun Alarm.toEntity(): AlarmEntity =
    AlarmEntity(
        id = id,
        name = name,
        time = time,
        weekDays = weekDays.joinToString { it.value.toString() },
        isOnce = isOnce,
        isActivated = isActivated,
    )

private fun AlarmTemplateEntity.toDomain(): AlarmTemplate =
    AlarmTemplate(
        name = name,
        time = time,
        weekDays = weekDays.toWeekDays(),
    )

private fun AlarmTemplate.toEntity(): AlarmTemplateEntity =
    AlarmTemplateEntity(
        name = name,
        time = time,
        weekDays = weekDays.joinToString { it.value.toString() },
    )
