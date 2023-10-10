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
import kotlinx.coroutines.flow.Flow

public interface AlarmRepository {
    public fun observeAlarms(): Flow<List<Alarm>>
    public suspend fun getAlarm(alarmId: Int): Alarm?
    public suspend fun saveAlarm(alarm: Alarm)
    public suspend fun deleteAlarm(alarmId: Int)
}
