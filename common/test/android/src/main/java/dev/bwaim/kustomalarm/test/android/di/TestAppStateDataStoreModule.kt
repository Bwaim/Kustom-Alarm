/*
 * Copyright 2024 Dev Bwaim team
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

package dev.bwaim.kustomalarm.test.android.di

import android.annotation.SuppressLint
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.bwaim.kustomalarm.settings.impl.appstate.AppStatePreferences
import dev.bwaim.kustomalarm.settings.impl.appstate.AppStatePreferencesSerializer
import dev.bwaim.kustomalarm.settings.impl.appstate.di.AppStateDatastoreModule
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppStateDatastoreModule::class],
)
internal object TestAppStateDataStoreModule {
    @Provides
    @Singleton
    fun providesAppStatePreferencesDataStore(
        appStatePreferencesSerializer: AppStatePreferencesSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<AppStatePreferences> = tmpFolder.testAppStatePreferencesDataStore(appStatePreferencesSerializer)
}

@SuppressLint("VisibleForTests")
public fun TemporaryFolder.testAppStatePreferencesDataStore(
    appStatePreferencesSerializer: AppStatePreferencesSerializer = AppStatePreferencesSerializer(),
): DataStore<AppStatePreferences> =
    DataStoreFactory.create(
        serializer = appStatePreferencesSerializer,
    ) {
        create()
        newFile("app_state_preferences_test.pb")
    }
