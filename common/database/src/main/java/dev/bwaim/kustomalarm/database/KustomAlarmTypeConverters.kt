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

package dev.bwaim.kustomalarm.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@ProvidedTypeConverter
internal class KustomAlarmTypeConverters @Inject constructor() {
    @TypeConverter
    fun fromLocalTime(localTime: LocalTime): String = localTime.format(DateTimeFormatter.ISO_LOCAL_TIME)

    @TypeConverter
    fun toLocalTime(value: String): LocalTime = LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME)

    @TypeConverter
    fun fromNullableLocalTime(localTime: LocalTime?): String? = localTime?.format(DateTimeFormatter.ISO_LOCAL_TIME)

    @TypeConverter
    fun toNullableLocalTime(value: String?): LocalTime? =
        value ?.let { LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME) }

    @TypeConverter
    fun fromDuration(duration: Duration): Long = duration.inWholeMilliseconds

    @TypeConverter
    fun toDuration(milliseconds: Long): Duration = milliseconds.milliseconds
}
