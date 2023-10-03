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

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.bwaim.kustomalarm.database.alarm.AlarmDao
import dev.bwaim.kustomalarm.database.alarm.AlarmEntity

internal const val DATABASE_VERSION = 1

@Database(
    entities = [
        AlarmEntity::class,
    ],
    version = DATABASE_VERSION,
    exportSchema = true,
    autoMigrations = [],
)
internal abstract class KustomAlarmRoomDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    internal companion object {
        const val DATABASE_NAME = "KustomAlarm-app.db"
    }
}
