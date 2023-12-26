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

package dev.bwaim.kustomalarm.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.localisation.R.string

@Composable
public fun KaAlertDialog(
    text: String,
    confirmTextButton: String,
    dismissTextButton: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    AlertDialog(
        text = { Text(text = text) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = confirmTextButton)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = dismissTextButton)
            }
        },
    )
}

@Composable
public fun KaConfirmDiscardChangesAlertDialog(
    text: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    KaAlertDialog(
        text = text,
        confirmTextButton = stringResource(id = string.global_action_save),
        dismissTextButton = stringResource(id = string.global_action_ignore),
        onConfirmation = onConfirmation,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
@PreviewsKAlarm
private fun PreviewKaConfirmDiscardChangesAlertDialog() {
    KustomAlarmThemePreview {
        KaConfirmDiscardChangesAlertDialog(text = "Text for an alert dialog")
    }
}
