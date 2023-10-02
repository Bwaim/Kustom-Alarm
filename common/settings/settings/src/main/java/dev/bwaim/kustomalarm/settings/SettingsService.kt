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

package dev.bwaim.kustomalarm.settings

import dev.bwaim.kustomalarm.core.IODispatcher
import dev.bwaim.kustomalarm.settings.theme.ThemeRepository
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

public class SettingsService @Inject public constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val themeRepository: ThemeRepository,
) {
    public fun observeTheme(): Flow<Theme> {
        return themeRepository
            .observeTheme()
            .flowOn(ioDispatcher)
    }

    public suspend fun setTheme(theme: Theme) {
        withContext(ioDispatcher) {
            themeRepository.setTheme(theme)
        }
    }

    public fun getThemes(): List<Theme> = Theme.values().toList()

    public fun getLocales(): List<Locale> = listOf(
        Locale("es"),
        Locale.ENGLISH,
        Locale.FRENCH,
    )
}
