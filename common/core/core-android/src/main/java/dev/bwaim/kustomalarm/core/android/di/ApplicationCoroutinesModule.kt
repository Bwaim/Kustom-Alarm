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

package dev.bwaim.kustomalarm.core.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bwaim.kustomalarm.core.ApplicationScope
import dev.bwaim.kustomalarm.core.ComputationDispatcher
import dev.bwaim.kustomalarm.core.IODispatcher
import dev.bwaim.kustomalarm.core.MainDispatcher
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
internal object ApplicationCoroutinesModule {
    @Provides
    @Singleton
    @ApplicationScope
    internal fun provideApplicationScope(
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + mainDispatcher)

    @Provides
    @MainDispatcher
    internal fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

    @Provides @IODispatcher internal fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @ComputationDispatcher
    internal fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
