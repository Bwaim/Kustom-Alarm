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

@file:OptIn(ExperimentalMaterial3Api::class)

package dev.bwaim.kustomalarm.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.compose.wheelpicker.WheelPicker
import kotlinx.collections.immutable.toPersistentList
import java.text.NumberFormat
import java.time.LocalTime

@Composable
public fun KaTimePicker(
    modifier: Modifier = Modifier,
    initialValue: LocalTime = LocalTime.of(7, 0),
    onValueChanged: (LocalTime) -> Unit = {},
) {
    var hours by remember { mutableIntStateOf(initialValue.hour) }
    var minutes by remember { mutableIntStateOf(initialValue.minute) }

    LaunchedEffect(hours, minutes, onValueChanged) {
        onValueChanged(LocalTime.of(hours, minutes))
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.6f),
            modifier = Modifier.clip(MaterialTheme.shapes.medium),
        ) {
            // Invisible row for sizing the Surface
            Row(
                modifier = Modifier
                    .alpha(0f)
                    .padding(horizontal = 10.dp),
            ) {
                TimeLabel(value = 23)
                TimeSeparator()
                TimeLabel(value = 59)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WheelPicker(
                items = (0..23).toPersistentList(),
                nbVisibleItems = 3,
                startIndex = hours,
                onValueChanged = { hour -> hours = hour },
            ) { item, itemModifier ->
                TimeLabel(value = item, modifier = itemModifier)
            }

            TimeSeparator()

            WheelPicker(
                items = (0..59).toPersistentList(),
                nbVisibleItems = 3,
                startIndex = minutes,
                onValueChanged = { minute -> minutes = minute },
            ) { item, itemModifier ->
                TimeLabel(value = item, modifier = itemModifier)
            }
        }
    }
}

@Composable
private fun TimeLabel(
    value: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = value.toLocalString(2),
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium,
    )
}

@Composable
private fun TimeSeparator() {
    Text(
        text = ":",
        modifier = Modifier.padding(horizontal = 20.dp),
        style = MaterialTheme.typography.displaySmall,
    )
}

private fun Int.toLocalString(minDigits: Int): String {
    val formatter = NumberFormat.getIntegerInstance()
    // Eliminate any use of delimiters when formatting the integer.
    formatter.isGroupingUsed = false
    formatter.minimumIntegerDigits = minDigits
    return formatter.format(this)
}

@Composable
@PreviewsKAlarm
private fun PreviewKaTimePicker() {
    KustomAlarmThemePreview { KaTimePicker() }
}
