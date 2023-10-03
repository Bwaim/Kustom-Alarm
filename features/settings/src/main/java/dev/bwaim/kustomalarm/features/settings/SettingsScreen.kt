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

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.bwaim.kustomalarm.compose.KAlarmPreviews
import dev.bwaim.kustomalarm.compose.KaBackTopAppBar
import dev.bwaim.kustomalarm.compose.KaBackground
import dev.bwaim.kustomalarm.compose.preference.model.ListPreferenceValues
import dev.bwaim.kustomalarm.compose.preference.model.Preference
import dev.bwaim.kustomalarm.compose.preference.ui.ListPreferenceWidget
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import kotlinx.collections.immutable.persistentMapOf
import java.util.Locale

@Composable
internal fun SettingsRoute(
    onClose: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val selectedTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
    val themes = viewModel.themes.value
    val locales = viewModel.locales.value

    SettingsScreen(
        selectedTheme = selectedTheme,
        themes = themes,
        locales = locales,
        onClose = onClose,
        onThemeChanged = viewModel::setTheme,
        onLocaleChanged = { AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(it.value)) },
    )
}

@Composable
private fun SettingsScreen(
    selectedTheme: Preference<Theme>,
    themes: ListPreferenceValues<Theme>,
    locales: ListPreferenceValues<Locale>,
    onClose: () -> Unit,
    onThemeChanged: (Preference<Theme>) -> Unit,
    onLocaleChanged: (Preference<Locale>) -> Unit,
) {
    Scaffold(
        topBar = {
            KaBackTopAppBar(
                onClickNavigation = onClose,
                title = {
                    Text(text = stringResource(id = string.settings_screen_label))
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
        ) {
            ListPreferenceWidget(
                preferences = themes,
                currentValue = selectedTheme,
                onValueChanged = onThemeChanged,
            )

            ListPreferenceWidget(
                preferences = locales,
                currentValue = getCurrentLocale(),
                onValueChanged = onLocaleChanged,
            )
        }
    }
}

@Composable
private fun getCurrentLocale(): Preference<Locale> =
    (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).toPreference()

@Composable
@KAlarmPreviews
private fun PreviewSettingsScreen() {
    KustomAlarmTheme {
        KaBackground {
            val currentValue = Preference(label = "dark", value = Theme.DARK)

            val preferences = persistentMapOf(
                "light" to Preference(label = "light", value = Theme.LIGHT),
                "dark" to currentValue,
                "system" to Preference(label = "system", value = Theme.SYSTEM),
            )
            val listPreference = ListPreferenceValues(
                title = "Theme",
                entries = preferences,
            )

            val locales = persistentMapOf(
                "English" to Preference(label = "english", value = Locale.ENGLISH),
                "Français" to Preference(label = "Français", value = Locale.FRENCH),
                "Español" to Preference(label = "Español", value = Locale("es")),
            )

            val listLocalesPreference = ListPreferenceValues(
                title = "Choose language",
                entries = locales,
            )

            SettingsScreen(
                selectedTheme = currentValue,
                themes = listPreference,
                locales = listLocalesPreference,
                onClose = {},
                onThemeChanged = {},
                onLocaleChanged = {},
            )
        }
    }
}
