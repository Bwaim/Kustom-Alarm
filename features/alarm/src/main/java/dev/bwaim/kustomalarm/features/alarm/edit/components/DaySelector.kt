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

package dev.bwaim.kustomalarm.features.alarm.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.KaFilterChip
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.extensions.testIdentifier
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.core.extentions.shortName
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import java.time.DayOfWeek
import java.util.Locale

@Composable
internal fun KaDaySelector(
    modifier: Modifier = Modifier,
    initialValue: PersistentSet<DayOfWeek> = persistentSetOf(),
    locale: Locale = Locale.ENGLISH,
    onValueChanged: (Set<DayOfWeek>) -> Unit = {},
) {
    val daysLabel =
        remember {
            DayOfWeek.entries.map { it to it.shortName(locale) }.toPersistentList()
        }

    val selectedDays: MutableSet<DayOfWeek> =
        remember {
            initialValue.toMutableSet()
        }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        daysLabel.forEach { (value, label) ->
            KaFilterChip(
                modifier = Modifier.testIdentifier(value.name),
                label = { labelModifier ->
                    Text(
                        modifier = labelModifier,
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                initialSelected = selectedDays.contains(value),
                onClick = {
                    if (selectedDays.contains(value)) {
                        selectedDays.remove(value)
                    } else {
                        selectedDays.add(value)
                    }
                    onValueChanged(selectedDays)
                },
            )
        }
    }
}

@Composable
@PreviewsKAlarm
private fun KaDaySelectorPreview() {
    KustomAlarmThemePreview {
        Column {
            KaDaySelector(modifier = Modifier.padding(5.dp), locale = Locale.ENGLISH)
            KaDaySelector(modifier = Modifier.padding(5.dp), locale = Locale.FRENCH)
            KaDaySelector(modifier = Modifier.padding(5.dp), locale = Locale.forLanguageTag("es"))
        }
    }
}
