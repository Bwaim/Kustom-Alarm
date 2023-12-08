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
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.bwaim.kustomalarm.compose.KaBackTopAppBar
import dev.bwaim.kustomalarm.compose.KaBackground
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.preference.model.ListPreferenceValues
import dev.bwaim.kustomalarm.compose.preference.model.Preference
import dev.bwaim.kustomalarm.compose.preference.ui.ListPreferenceWidget
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.core.android.BuildWrapper
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import java.util.Locale

@Composable
internal fun SettingsRoute(
    onClose: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val selectedTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
    val themesList = viewModel.themes.value
    val localesList = viewModel.locales.value

    val context = LocalContext.current
    val selectedThemePref = remember(selectedTheme, context) { selectedTheme.toPreference(context) }
    val themes = remember(themesList, context) { themesList.toThemeListPreferences(context) }
    val locales = remember(localesList, context) { localesList.toLocaleListPreferences(context) }

    SettingsScreen(
        selectedTheme = selectedThemePref,
        themes = themes,
        locales = locales,
        onClose = onClose,
        onThemeChanged = viewModel::setTheme,
        onLocaleChanged = {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(it.value))
        },
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
                title = { Text(text = stringResource(id = string.settings_screen_label)) },
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

private fun Theme.getLabel(context: Context): String =
    when (this) {
        Theme.LIGHT -> context.getString(string.settings_screen_theme_light_label)
        Theme.DARK -> context.getString(string.settings_screen_theme_dark_label)
        Theme.SYSTEM -> context.getString(string.settings_screen_theme_system_label)
        Theme.BATTERY_SAVER -> context.getString(string.settings_screen_theme_battery_label)
    }

private fun Theme.toPreference(context: Context): Preference<Theme> = Preference(label = this.getLabel(context), value = this)

private fun List<Theme>.toThemeListPreferences(context: Context): ListPreferenceValues<Theme> =
    ListPreferenceValues(
        title = context.getString(string.settings_screen_theme_title),
        entries =
            this.filter { it.isAvailable() }
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

@Composable
private fun getCurrentLocale(): Preference<Locale> = (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).toPreference()

private fun List<Locale>.toLocaleListPreferences(context: Context): ListPreferenceValues<Locale> {
    val applicationLocales =
        LocaleListCompat.create(
            *(this.toTypedArray()),
        )
    val locales: MutableMap<String, Preference<Locale>> = mutableMapOf()
    for (i in 0 until applicationLocales.size()) {
        with(applicationLocales[i]) {
            this?.run {
                val locale = this.toPreference()
                locales.put(locale.label, locale)
            }
        }
    }
    return ListPreferenceValues(
        title = context.getString(string.settings_screen_locale_title),
        entries = locales.toImmutableMap(),
    )
}

private fun Locale.toPreference(): Preference<Locale> = Preference(label = getDisplayLanguage(this), value = this)

@Composable
@PreviewsKAlarm
private fun PreviewSettingsScreen() {
    KustomAlarmTheme {
        KaBackground {
            val currentValue = Preference(label = "dark", value = Theme.DARK)

            val preferences =
                persistentMapOf(
                    "light" to Preference(label = "light", value = Theme.LIGHT),
                    "dark" to currentValue,
                    "system" to Preference(label = "system", value = Theme.SYSTEM),
                )
            val listPreference =
                ListPreferenceValues(
                    title = "Theme",
                    entries = preferences,
                )

            val locales =
                persistentMapOf(
                    "English" to Preference(label = "english", value = Locale.ENGLISH),
                    "Français" to Preference(label = "Français", value = Locale.FRENCH),
                    "Español" to Preference(label = "Español", value = Locale("es")),
                )

            val listLocalesPreference =
                ListPreferenceValues(
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
