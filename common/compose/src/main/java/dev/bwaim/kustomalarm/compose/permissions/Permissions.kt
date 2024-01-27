/*
 * Copyright 2024 Dev Bwaim team
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

@file:OptIn(ExperimentalPermissionsApi::class)

package dev.bwaim.kustomalarm.compose.permissions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.PermissionStatus.Granted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dev.bwaim.kustomalarm.localisation.R.string

public data class PermissionScreenState(
    val permissionState: PermissionState,
    val title: String,
    val rationale: String,
    var showRationale: Boolean = false,
)

@Composable
public fun PermissionScreen(
    permission: String,
    title: String,
    rationale: String,
    isApplicable: Boolean = true,
    onPermissionResult: (Boolean) -> Unit = {},
    content: @Composable (() -> Unit) -> Unit,
) {
    val permissionState = if (isApplicable) {
        rememberPermissionState(
            permission = permission,
            onPermissionResult = onPermissionResult,
        )
    } else {
        null
    }

    var permissionScreenState by remember(permissionState) {
        mutableStateOf(
            permissionState?.let {
                PermissionScreenState(
                    permissionState = permissionState,
                    title = title,
                    rationale = rationale,
                )
            },
        )
    }

    val permissionActionTrigger = remember {
        {
            permissionScreenState.checkPermission(
                action = onPermissionResult,
                onShouldShowRationale = {
                    permissionScreenState =
                        permissionScreenState?.copy(showRationale = true)
                },
            )
        }
    }

    content(permissionActionTrigger)

    permissionScreenState?.let {
        ShowRationale(
            permissionScreenState = it,
            onRationaleReply = { accepted ->
                permissionScreenState = it.copy(showRationale = false)
                if (!accepted) {
                    onPermissionResult(false)
                }
            },
        )
    }
}

private fun PermissionScreenState?.checkPermission(
    action: (Boolean) -> Unit,
    onShouldShowRationale: () -> Unit = {},
) {
    if (this == null) {
        action.invoke(true)
        return
    }
    when (permissionState.status) {
        Granted -> action.invoke(true)

        is Denied -> if (permissionState.status.shouldShowRationale) {
            onShouldShowRationale()
        } else {
            permissionState.launchPermissionRequest()
        }
    }
}

@Composable
private fun ShowRationale(
    permissionScreenState: PermissionScreenState,
    onRationaleReply: (Boolean) -> Unit,
) {
    if (permissionScreenState.showRationale) {
        AlertDialog(
            onDismissRequest = { onRationaleReply(false) },
            title = {
                Text(text = permissionScreenState.title)
            },
            text = {
                Text(text = permissionScreenState.rationale)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        permissionScreenState.permissionState.launchPermissionRequest()
                        onRationaleReply(true)
                    },
                ) {
                    Text(text = stringResource(id = string.permission_global_continue))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onRationaleReply(false) },
                ) {
                    Text(text = stringResource(id = string.permission_global_dismiss))
                }
            },
        )
    }
}
