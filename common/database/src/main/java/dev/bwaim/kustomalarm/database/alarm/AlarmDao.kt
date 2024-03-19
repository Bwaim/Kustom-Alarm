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

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM ALARM_ENTITY")
    public fun observeAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM ALARM_ENTITY WHERE POSTPONE_TIME != 0")
    public fun observeSnoozedAlarm(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM ALARM_ENTITY WHERE _id = :id")
    public suspend fun getAlarm(id: Int): AlarmEntity?

    @Upsert public suspend fun upsertAlarm(alarmEntity: AlarmEntity)

    @Query("DELETE FROM ALARM_ENTITY WHERE _id = :alarmId")
    public suspend fun deleteAlarm(alarmId: Int)

    @Upsert public suspend fun upsertAlarmTemplate(alarmTemplateEntity: AlarmTemplateEntity)

    @Query("SELECT * FROM ALARM_TEMPLATE_ENTITY LIMIT 1")
    public suspend fun getAlarmTemplate(): AlarmTemplateEntity?
}
