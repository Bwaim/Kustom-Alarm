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

package dev.bwaim.kustomalarm.features.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bwaim.kustomalarm.compose.preference.model.Preference
import dev.bwaim.kustomalarm.settings.SettingsService
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel
    @Inject
    constructor(
        private val settingsService: SettingsService,
    ) : ViewModel() {
        val themes: State<List<Theme>> = mutableStateOf(settingsService.getThemes())

        val currentTheme: StateFlow<Theme> =
            settingsService
                .observeTheme()
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    Theme.LIGHT,
                )

        val locales: State<List<Locale>> = mutableStateOf(settingsService.getLocales())

        public fun setTheme(theme: Preference<Theme>) {
            viewModelScope.launch { settingsService.setTheme(theme.value) }
        }
    }
