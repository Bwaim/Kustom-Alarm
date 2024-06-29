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

package dev.bwaim.kustomalarm.alarm

import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.alarm.domain.AlarmTemplate
import dev.bwaim.kustomalarm.alarm.domain.TEMPORAL_ALARM_ID
import dev.bwaim.kustomalarm.core.DomainResult
import dev.bwaim.kustomalarm.core.IODispatcher
import dev.bwaim.kustomalarm.core.executeCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

public class AlarmService @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val alarmRepository: AlarmRepository,
) {
    public fun observeAlarms(): Flow<List<Alarm>> = alarmRepository.observeAlarms().flowOn(ioDispatcher)

    public fun observeSnoozedAlarm(): Flow<Alarm?> = alarmRepository.observeSnoozedAlarm().flowOn(ioDispatcher)

    public suspend fun getAlarm(alarmId: Int): DomainResult<Alarm?> =
        executeCatching(ioDispatcher) { alarmRepository.getAlarm(alarmId = alarmId) }

    public suspend fun getDefaultAlarm(): DomainResult<Alarm> =
        executeCatching(ioDispatcher) {
            alarmRepository.getTemplate().toAlarm()
        }

    public suspend fun saveAlarm(alarm: Alarm): DomainResult<Unit> =
        executeCatching(ioDispatcher) { alarmRepository.saveAlarm(alarm.completeDefault()) }

    public suspend fun saveTemporalAlarm(alarm: Alarm): DomainResult<Unit> =
        executeCatching(ioDispatcher) {
            alarmRepository.saveAlarm(alarm.copy(id = TEMPORAL_ALARM_ID).completeDefault())
        }

    public suspend fun deleteAlarm(alarmId: Int): DomainResult<Unit> =
        executeCatching(ioDispatcher) { alarmRepository.deleteAlarm(alarmId = alarmId) }

    private fun Alarm.completeDefault(): Alarm {
        var isOnce = this.isOnce

        val currentDayOfWeek = LocalDate.now().dayOfWeek
        val daysOfWeek =
            this.weekDays.ifEmpty {
                isOnce = true
                if (LocalTime.now().isBefore(time)) {
                    setOf(currentDayOfWeek)
                } else {
                    setOf(currentDayOfWeek.plus(1))
                }
            }

        return this.copy(weekDays = daysOfWeek, isOnce = isOnce)
    }

    public suspend fun saveTemplate(alarmTemplate: AlarmTemplate) {
        executeCatching(ioDispatcher) {
            alarmRepository.saveTemplate(alarmTemplate)
        }
    }
}

internal fun AlarmTemplate.toAlarm(): Alarm =
    Alarm(
        name = name,
        time = time,
        weekDays = weekDays,
        uri = uri,
    )
