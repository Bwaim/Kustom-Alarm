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

package dev.bwaim.kustomalarm.features.alarm.edit

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.KaCloseTopAppBar
import dev.bwaim.kustomalarm.compose.KaLargeTextField
import dev.bwaim.kustomalarm.compose.KaTimePicker
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.localisation.R.string

@Composable
internal fun EditAlarmRoute(close: () -> Unit) {
    EditAlarmScreen(
        close = close,
    )
}

@Composable
private fun EditAlarmScreen(close: () -> Unit) {
    Scaffold(
        topBar = {
            KaCloseTopAppBar(
                onClickNavigation = close,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp),
        ) {
            AlarmName()
            KaTimePicker(
                modifier = Modifier.padding(vertical = 5.dp),
                onValueChanged = { Log.d("FBU", "hour $it") },
            )
        }
    }
}

@Composable
private fun AlarmName() {
    val name = remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val context = LocalContext.current
    val label by
        remember(name) {
            derivedStateOf {
                if (name.value.isEmpty() && isFocused.not()) {
                    context.getString(string.edit_alarm_screen_alarm_name_label)
                } else {
                    null
                }
            }
        }
    KaLargeTextField(
        value = name.value,
        onValueChange = { name.value = it },
        modifier = Modifier.fillMaxWidth(),
        label = label,
        interactionSource = interactionSource,
    )
}

@Composable
@PreviewsKAlarm
private fun PreviewEditAlarmScreen() {
    KustomAlarmThemePreview {
        EditAlarmScreen(
            close = {},
        )
    }
}
