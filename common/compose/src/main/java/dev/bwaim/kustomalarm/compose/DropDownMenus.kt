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

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.localisation.R.string

@Composable
public fun DeleteDropDownMenuItem(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(id = string.global_action_delete)) },
        onClick = onDelete,
        modifier = modifier,
    )
}

@Composable
public fun ModifyDropDownMenuItem(
    onModify: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(id = string.global_action_modify)) },
        onClick = onModify,
        modifier = modifier,
    )
}

@Composable
public fun DuplicateDropDownMenuItem(
    onDuplicate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(id = string.global_action_duplicate)) },
        onClick = onDuplicate,
        modifier = modifier,
    )
}

@Composable
@PreviewsKAlarm
private fun PreviewDropDownMenu() {
    KustomAlarmThemePreview {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { },
        ) {
            DeleteDropDownMenuItem(onDelete = { })
            ModifyDropDownMenuItem(onModify = { })
            DuplicateDropDownMenuItem(onDuplicate = { })
        }
    }
}
