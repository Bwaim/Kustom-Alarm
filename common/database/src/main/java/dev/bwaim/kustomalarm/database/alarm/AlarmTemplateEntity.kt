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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity("ALARM_TEMPLATE_ENTITY")
public data class AlarmTemplateEntity(
    @PrimaryKey @ColumnInfo(name = "_id") val id: Int = 1,
    @ColumnInfo(name = "NAME") val name: String?,
    @ColumnInfo(name = "ALARM_TIME") val time: LocalTime,
    @ColumnInfo(name = "WEEKDAYS") val weekDays: String,
    @ColumnInfo(name = "ALARM_URI") val uri: String,
)
