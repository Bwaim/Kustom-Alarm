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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.compose.KaCloseTopAppBar
import dev.bwaim.kustomalarm.compose.KaLargeTextField
import dev.bwaim.kustomalarm.compose.KaTimePicker
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.PrimaryButton
import dev.bwaim.kustomalarm.compose.SaveEventsEffect
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.core.android.extensions.getAppLocale
import dev.bwaim.kustomalarm.core.android.extensions.toast
import dev.bwaim.kustomalarm.features.alarm.edit.components.KaDaySelector
import dev.bwaim.kustomalarm.localisation.R.string
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import java.time.DayOfWeek
import java.time.LocalTime

@Composable
internal fun EditAlarmRoute(
    close: () -> Unit,
    editViewModel: EditViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    SaveEventsEffect(
        eventFlow = editViewModel.saveEventsFlow,
        successAction = {
            context.toast(it)
            close()
        },
        failureAction = { context.toast(it) },
    )

    EditAlarmScreen(
        close = close,
        onSave = editViewModel::saveAlarm,
    )
}

@Composable
private fun EditAlarmScreen(
    close: () -> Unit,
    onSave: (Alarm) -> Unit,
) {
    val context = LocalContext.current
    val currentLocale = remember { context.getAppLocale() }

    val alarmName: MutableState<String?> =
        remember {
            mutableStateOf(null)
        }
    val alarmTime: MutableState<LocalTime> =
        remember {
            mutableStateOf(LocalTime.of(7, 0))
        }
    val daysOfWeek: MutableState<PersistentSet<DayOfWeek>> =
        remember {
            mutableStateOf(persistentSetOf())
        }

    Scaffold(
        topBar = {
            KaCloseTopAppBar(onClickNavigation = close)
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
        ) {
            AlarmName(
                name = alarmName.value,
                onValueChange = { alarmName.value = it },
            )
            KaTimePicker(
                modifier = Modifier.padding(vertical = 5.dp),
                onValueChanged = { alarmTime.value = it },
            )
            KaDaySelector(
                locale = currentLocale,
                onValueChanged = { daysOfWeek.value = it.toPersistentSet() },
            )

            PrimaryButton(
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp),
                text = stringResource(id = string.global_action_save),
                onClick = {
                    onSave(
                        Alarm(
                            name = alarmName.value,
                            time = alarmTime.value,
                            weekDays = daysOfWeek.value,
                        ),
                    )
                },
            )
        }
    }
}

@Composable
private fun AlarmName(
    name: String?,
    onValueChange: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val context = LocalContext.current
    val label by
        remember(name) {
            derivedStateOf {
                if (name.isNullOrBlank() && isFocused.not()) {
                    context.getString(string.edit_alarm_screen_alarm_name_label)
                } else {
                    null
                }
            }
        }
    KaLargeTextField(
        value = name,
        onValueChange = onValueChange,
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
            onSave = {},
        )
    }
}
