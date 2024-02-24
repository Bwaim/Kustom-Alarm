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

import android.Manifest.permission
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.bwaim.kustomalarm.compose.KaCloseErrorMessage
import dev.bwaim.kustomalarm.compose.KaCloseTopAppBar
import dev.bwaim.kustomalarm.compose.KaConfirmDiscardChangesAlertDialog
import dev.bwaim.kustomalarm.compose.KaLargeTextField
import dev.bwaim.kustomalarm.compose.KaLoader
import dev.bwaim.kustomalarm.compose.KaTimePicker
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.PrimaryButton
import dev.bwaim.kustomalarm.compose.SaveEventsEffect
import dev.bwaim.kustomalarm.compose.permissions.PermissionScreen
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.core.android.BuildWrapper
import dev.bwaim.kustomalarm.core.android.extensions.getAppLocale
import dev.bwaim.kustomalarm.core.android.extensions.toast
import dev.bwaim.kustomalarm.features.alarm.edit.components.AlarmMoreMenu
import dev.bwaim.kustomalarm.features.alarm.edit.components.KaDaySelector
import dev.bwaim.kustomalarm.features.alarm.edit.components.SoundSelector
import dev.bwaim.kustomalarm.features.alarm.edit.domain.AlarmUi
import dev.bwaim.kustomalarm.localisation.R.string
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import java.time.DayOfWeek
import java.time.LocalTime

@SuppressLint("InlinedApi")
@Composable
internal fun EditAlarmRoute(
    selectedUri: String?,
    cleanBackstack: () -> Unit,
    close: () -> Unit,
    onSoundSelectionClick: (String) -> Unit,
    previewAlarm: (Int) -> Unit,
    editViewModel: EditViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = selectedUri, cleanBackstack) {
        if (selectedUri != null) {
            editViewModel.updateUri(uri = selectedUri)
            cleanBackstack()
        }
    }

    val context = LocalContext.current
    SaveEventsEffect(
        eventFlow = editViewModel.saveEventsFlow,
        successAction = {
            context.toast(it)
            close()
        },
        failureAction = { context.toast(it) },
    )

    val alarm by editViewModel.alarm.collectAsStateWithLifecycle()
    val errorMessage by editViewModel.errorMessage.collectAsStateWithLifecycle()

    var showModificationMessage by remember {
        mutableStateOf(false)
    }

    val internalClose =
        remember(editViewModel) {
            {
                showModificationMessage =
                    if (editViewModel.hasModification() && !showModificationMessage) {
                        true
                    } else {
                        close()
                        false
                    }
            }
        }

    BackHandler {
        internalClose()
    }

    PermissionScreen(
        permission = permission.POST_NOTIFICATIONS,
        title = context.getString(string.permission_notification_titre),
        rationale = context.getString(string.permission_notification_rationale),
        isApplicable = BuildWrapper.isAtLeastT,
    ) { permissionTrigger ->

        LaunchedEffect(Unit) {
            permissionTrigger()
        }

        EditAlarmScreen(
            alarmUi = alarm,
            errorMessage = errorMessage,
            showModificationMessage = showModificationMessage,
            close = internalClose,
            onSoundSelectionClick = onSoundSelectionClick,
            onSave = editViewModel::saveAlarm,
            updateAlarmName = editViewModel::updateAlarmName,
            updateAlarmTime = editViewModel::updateAlarmTime,
            updateAlarmDays = editViewModel::updateAlarmDays,
            hideModificationMessage = { showModificationMessage = false },
            deleteAlarm = {
                editViewModel.deleteAlarm()
                close()
            },
            setTemplate = editViewModel::setTemplate,
            previewAlarm = {
                val alarmTmp = alarm
                alarmTmp?.let {
                    editViewModel.preview()
                    previewAlarm(alarmTmp.id)
                }
            },
        )
    }
}

@Composable
private fun EditAlarmScreen(
    alarmUi: AlarmUi?,
    errorMessage: String?,
    showModificationMessage: Boolean,
    close: () -> Unit,
    onSoundSelectionClick: (String) -> Unit,
    onSave: () -> Unit,
    updateAlarmName: (String) -> Unit,
    updateAlarmTime: (LocalTime) -> Unit,
    updateAlarmDays: (Set<DayOfWeek>) -> Unit,
    hideModificationMessage: () -> Unit,
    deleteAlarm: () -> Unit,
    setTemplate: () -> Unit,
    previewAlarm: () -> Unit,
) {
    Scaffold(
        topBar = {
            KaCloseTopAppBar(
                onClickNavigation = close,
                actions = {
                    Text(
                        text = stringResource(id = string.global_action_preview),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { previewAlarm() },
                    )
                    AlarmMoreMenu(
                        deleteAlarm = deleteAlarm,
                        setTemplate = setTemplate,
                    )
                },
            )
        },
    ) { padding ->
        when {
            errorMessage != null ->
                KaCloseErrorMessage(
                    errorMessage = errorMessage,
                    close = close,
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                )

            alarmUi == null ->
                KaLoader(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                )

            else ->
                AlarmDetails(
                    alarmUi = alarmUi,
                    onSoundSelectionClick = onSoundSelectionClick,
                    onSave = onSave,
                    updateAlarmName = updateAlarmName,
                    updateAlarmTime = updateAlarmTime,
                    updateAlarmDays = updateAlarmDays,
                    modifier = Modifier.padding(padding),
                )
        }
    }

    if (showModificationMessage) {
        KaConfirmDiscardChangesAlertDialog(
            text = stringResource(id = string.global_save_modifications_message),
            onConfirmation = {
                onSave()
                hideModificationMessage()
            },
            onDismissRequest = close,
        )
    }
}

@Composable
private fun AlarmDetails(
    alarmUi: AlarmUi,
    onSoundSelectionClick: (String) -> Unit,
    onSave: () -> Unit,
    updateAlarmName: (String) -> Unit,
    updateAlarmTime: (LocalTime) -> Unit,
    updateAlarmDays: (Set<DayOfWeek>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentLocale = remember { context.getAppLocale() }

    val alarmName: MutableState<String?> =
        remember {
            mutableStateOf(alarmUi.name)
        }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp),
    ) {
        AlarmName(
            name = alarmUi.name,
            onValueChange = {
                alarmName.value = it
                updateAlarmName(it)
            },
        )
        KaTimePicker(
            modifier = Modifier.padding(vertical = 5.dp),
            initialValue = alarmUi.time,
            onValueChanged = updateAlarmTime,
        )
        KaDaySelector(
            locale = currentLocale,
            initialValue = alarmUi.weekDays.toPersistentSet(),
            onValueChanged = { updateAlarmDays(it.toPersistentSet()) },
        )
        SoundSelector(
            title = alarmUi.ringtoneTitle,
            onSoundSelectionClick = { onSoundSelectionClick(alarmUi.uri) },
        )

        PrimaryButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 40.dp),
            text = stringResource(id = string.global_action_save),
            onClick = onSave,
        )
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
        maxLines = 1,
    )
}

@Composable
@PreviewsKAlarm
private fun PreviewEditAlarmScreen() {
    KustomAlarmThemePreview {
        EditAlarmScreen(
            alarmUi = AlarmUi(
                name = null,
                time = LocalTime.of(7, 0),
                weekDays = persistentSetOf(),
                uri = "uri1",
                ringtoneTitle = "Sound title",
            ),
            errorMessage = null,
            showModificationMessage = false,
            close = {},
            onSoundSelectionClick = {},
            onSave = {},
            updateAlarmName = {},
            updateAlarmTime = {},
            updateAlarmDays = {},
            hideModificationMessage = {},
            deleteAlarm = {},
            setTemplate = {},
            previewAlarm = {},
        )
    }
}
