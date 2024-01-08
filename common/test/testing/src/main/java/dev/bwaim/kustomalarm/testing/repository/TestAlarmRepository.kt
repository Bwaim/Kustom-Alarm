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

package dev.bwaim.kustomalarm.testing.repository

import dev.bwaim.kustomalarm.alarm.AlarmRepository
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.alarm.domain.AlarmTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.LocalTime

public val defaultTemplate: AlarmTemplate =
    AlarmTemplate(
        name = null,
        time = LocalTime.of(7, 0),
        weekDays = emptySet(),
        uri = "defaultUri",
    )

public class TestAlarmRepository : AlarmRepository {
    private var alarmStateFlow: MutableStateFlow<List<Alarm>> = MutableStateFlow(emptyList())
    private var nextId = 1

    private var templateStateFlow: MutableStateFlow<AlarmTemplate> =
        MutableStateFlow(defaultTemplate)

    override fun observeAlarms(): Flow<List<Alarm>> = alarmStateFlow.map { it.sortedBy { alarm -> alarm.id } }

    override suspend fun getAlarm(alarmId: Int): Alarm? = alarmStateFlow.value.firstOrNull { it.id == alarmId }

    override suspend fun saveAlarm(alarm: Alarm) {
        val updatedAlarm =
            if (alarm.id == 0) {
                alarm.copy(id = nextId++)
            } else {
                alarm
            }
        alarmStateFlow.update { (listOf(updatedAlarm) + it).distinctBy(Alarm::id) }
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        alarmStateFlow.update { it.filterNot { it.id == alarmId } }
    }

    override suspend fun saveTemplate(alarmTemplate: AlarmTemplate) {
        templateStateFlow.value = alarmTemplate
    }

    override suspend fun getTemplate(): AlarmTemplate {
        return templateStateFlow.value
    }
}
