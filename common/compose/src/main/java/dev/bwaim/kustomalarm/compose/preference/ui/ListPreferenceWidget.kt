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

package dev.bwaim.kustomalarm.compose.preference.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.extensions.testIdentifier
import dev.bwaim.kustomalarm.compose.preference.model.ListPreferenceValues
import dev.bwaim.kustomalarm.compose.preference.model.Preference
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import kotlinx.collections.immutable.persistentMapOf

@Composable
public fun <T> ListPreferenceWidget(
    preferences: ListPreferenceValues<T>,
    currentValue: Preference<T>,
    onValueChange: (value: Preference<T>) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListPreferenceUi(
        modifier = modifier,
        preferences = preferences,
        currentValue = currentValue,
        onValueChange = onValueChange,
    )
}

@Composable
private fun <T> ListPreferenceUi(
    preferences: ListPreferenceValues<T>,
    currentValue: Preference<T>,
    modifier: Modifier = Modifier,
    onValueChange: (value: Preference<T>) -> Unit,
) {
    var isDialogShown by remember { mutableStateOf(false) }
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable {
                isDialogShown = true
            },
    ) {
        Text(
            text = preferences.title,
        )
        Text(
            text = currentValue.label,
        )
    }

    if (isDialogShown) {
        ListPreferenceDialog(
            preferences = preferences,
            currentValue = currentValue,
            onDismiss = { isDialogShown = false },
            onSelect = { preference ->
                onValueChange(preference)
                isDialogShown = false
            },
        )
    }
}

@Composable
private fun <T> ListPreferenceDialog(
    preferences: ListPreferenceValues<T>,
    currentValue: Preference<T>,
    onDismiss: () -> Unit,
    onSelect: (Preference<T>) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = preferences.title,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        text = {
            Column(
                Modifier.fillMaxWidth(),
            ) {
                var index = 0
                preferences.entries.forEach { preference ->
                    val isSelected = preference.key == currentValue.label
                    val onSelectedAction = {
                        if (!isSelected) {
                            onSelect(preference.value)
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                onClick = { onSelectedAction() },
                            )
                            .testIdentifier("item_${index++}"),
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onSelectedAction() },
                        )
                        Text(
                            text = preference.value.label,
                            modifier = Modifier.align(CenterVertically),
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .testIdentifier("cancel_button"),
            ) {
                Text(
                    text = stringResource(android.R.string.cancel),
                )
            }
        },
    )
}

@Composable
@PreviewsKAlarm
private fun PreviewListPreference() {
    val currentValue = Preference(label = "pref2", value = "pref2Value")
    val preferences =
        persistentMapOf(
            "pref1" to Preference(label = "pref1", value = "pref1Value"),
            "pref2" to currentValue,
            "pref3" to Preference(label = "pref3", value = "pref3Value"),
        )
    val listPreference =
        ListPreferenceValues(
            title = "MyListPref",
            entries = preferences,
        )
    KustomAlarmThemePreview {
        ListPreferenceWidget(
            preferences = listPreference,
            currentValue = currentValue,
            onValueChange = {},
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewAlertListPref() {
    val currentValue = Preference(label = "pref2", value = "pref2Value")
    val preferences =
        persistentMapOf(
            "pref1" to Preference(label = "pref1", value = "pref1Value"),
            "pref2" to currentValue,
            "pref3" to Preference(label = "pref3", value = "pref3Value"),
        )
    val listPreference =
        ListPreferenceValues(
            title = "MyListPref",
            entries = preferences,
        )
    KustomAlarmThemePreview {
        ListPreferenceDialog(
            preferences = listPreference,
            currentValue = currentValue,
            onDismiss = {},
            onSelect = {},
        )
    }
}
