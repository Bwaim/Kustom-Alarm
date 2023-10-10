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

package dev.bwaim.kustomalarm.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.bwaim.kustomalarm.core.IODispatcher
import dev.bwaim.kustomalarm.database.KustomAlarmRoomDatabase
import dev.bwaim.kustomalarm.database.KustomAlarmTypeConverters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DatabaseModule {
    @Binds
    abstract fun bindRoomDatabase(database: KustomAlarmRoomDatabase): RoomDatabase

    companion object {
        @Provides
        @Singleton
        fun provideKustomAlarmRoomDatabase(
            @ApplicationContext context: Context,
            @IODispatcher ioDispatchers: CoroutineDispatcher,
            typeConverters: KustomAlarmTypeConverters,
        ): KustomAlarmRoomDatabase {
            val roomExecutor = ioDispatchers.asExecutor()
            return Room
                .databaseBuilder(
                    context,
                    KustomAlarmRoomDatabase::class.java,
                    KustomAlarmRoomDatabase.DATABASE_NAME,
                )
                .addTypeConverter(typeConverters)
                .setQueryExecutor(roomExecutor)
                .setTransactionExecutor(roomExecutor)
                .build()
        }

        @Provides
        fun provideAlarmDao(database: KustomAlarmRoomDatabase) = database.alarmDao()
    }
}
