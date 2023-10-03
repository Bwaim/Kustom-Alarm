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

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.compose.preference.model.ListPreferenceValues
import dev.bwaim.kustomalarm.compose.preference.model.Preference
import dev.bwaim.kustomalarm.core.android.BuildWrapper
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.settings.SettingsService
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val settingsService: SettingsService,
) : ViewModel() {

    val themes: State<ListPreferenceValues<Theme>> = mutableStateOf(
        settingsService.getThemes().toThemeListPreferences(appContext),
    )

    val currentTheme: StateFlow<Preference<Theme>> = settingsService.observeTheme()
        .map { it.toPreference(appContext) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            Theme.LIGHT.toPreference(appContext),
        )

    public fun setTheme(theme: Preference<Theme>) {
        viewModelScope.launch {
            settingsService.setTheme(theme.value)
        }
    }
}

private fun Theme.getLabel(context: Context): String =
    when (this) {
        Theme.LIGHT -> context.getString(string.settings_screen_theme_light_label)
        Theme.DARK -> context.getString(string.settings_screen_theme_dark_label)
        Theme.SYSTEM -> context.getString(string.settings_screen_theme_system_label)
        Theme.BATTERY_SAVER -> context.getString(string.settings_screen_theme_battery_label)
    }

private fun Theme.toPreference(context: Context): Preference<Theme> =
    Preference(label = this.getLabel(context), value = this)

private fun List<Theme>.toThemeListPreferences(context: Context): ListPreferenceValues<Theme> =
    ListPreferenceValues(
        title = context.getString(string.settings_screen_theme_title),
        entries = this
            .filter { it.isAvailable() }
            .associate {
                val preference = it.toPreference(context)
                preference.label to preference
            }
            .toImmutableMap(),
    )

private fun Theme.isAvailable(): Boolean =
    when (this) {
        Theme.LIGHT -> true
        Theme.DARK -> true
        Theme.SYSTEM -> BuildWrapper.isAtLeastQ
        Theme.BATTERY_SAVER -> BuildWrapper.isAtLeastQ.not()
    }
