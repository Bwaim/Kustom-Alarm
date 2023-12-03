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

package dev.bwaim.kustomalarm.alarm.impl.testdoubles

import dev.bwaim.kustomalarm.database.alarm.AlarmDao
import dev.bwaim.kustomalarm.database.alarm.AlarmEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class TestAlarmDao : AlarmDao {

    private var entitiesStateFlow =
        MutableStateFlow(
            emptyList<AlarmEntity>(),
        )

    private var nextId = 1

    override fun observeAlarms(): Flow<List<AlarmEntity>> {
        return entitiesStateFlow.map { it.sortedBy { alarm -> alarm.id } }
    }

    override suspend fun getAlarm(id: Int): AlarmEntity? {
        return entitiesStateFlow.value.firstOrNull { it.id == id }
    }

    override suspend fun upsertAlarm(alarmEntity: AlarmEntity) {
        val updatedAlarm =
            if (alarmEntity.id == 0) {
                alarmEntity.copy(id = nextId++)
            } else {
                alarmEntity
            }
        entitiesStateFlow.update { oldValues ->
            // New values come first so they overwrite old values
            (listOf(updatedAlarm) + oldValues).distinctBy(AlarmEntity::id)
        }
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        entitiesStateFlow.update { entities -> entities.filterNot { alarmId == it.id } }
    }
}
