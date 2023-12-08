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

package dev.bwaim.kustomalarm.test.android.di

import android.annotation.SuppressLint
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.bwaim.kustomalarm.settings.impl.theme.ThemePreferences
import dev.bwaim.kustomalarm.settings.impl.theme.ThemePreferencesSerializer
import dev.bwaim.kustomalarm.settings.impl.theme.di.ThemeDataStoreModule
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ThemeDataStoreModule::class],
)
internal object TestThemeDataStoreModule {
    @Provides
    @Singleton
    fun providesThemePreferencesDataStore(
        themePreferencesSerializer: ThemePreferencesSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<ThemePreferences> = tmpFolder.testThemePreferencesDataStore(themePreferencesSerializer)
}

@SuppressLint("VisibleForTests")
public fun TemporaryFolder.testThemePreferencesDataStore(
    themePreferencesSerializer: ThemePreferencesSerializer = ThemePreferencesSerializer(),
): DataStore<ThemePreferences> =
    DataStoreFactory.create(
        serializer = themePreferencesSerializer,
    ) {
        create()
        newFile("theme_preferences_test.pb")
    }
