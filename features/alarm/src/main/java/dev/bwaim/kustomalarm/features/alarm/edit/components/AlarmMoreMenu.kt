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
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bwaim.kustomalarm.compose.DeleteDropDownMenuItem
import dev.bwaim.kustomalarm.compose.DuplicateDropDownMenuItem
import dev.bwaim.kustomalarm.compose.ModifyDropDownMenuItem
import dev.bwaim.kustomalarm.compose.MoreActionIcon

@Composable
internal fun AlarmMoreMenu(
    deleteAlarm: () -> Unit,
    modifier: Modifier = Modifier,
    modifyAlarm: (() -> Unit)? = null,
    duplicateAlarm: (() -> Unit)? = null,
) {
    var moreMenuExpanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MoreActionIcon(
            onClick = { moreMenuExpanded = true },
        )
        EditDropDownMenu(
            expanded = moreMenuExpanded,
            onDismissRequest = { moreMenuExpanded = false },
            onModify = modifyAlarm,
            onDelete = deleteAlarm,
            onDuplicate = duplicateAlarm,
        )
    }
}

@Composable
private fun EditDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    onModify: (() -> Unit)? = null,
    onDuplicate: (() -> Unit)? = null,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        onModify?.let { ModifyDropDownMenuItem(onModify = onModify) }
        DeleteDropDownMenuItem(onDelete = onDelete)
        onDuplicate?.let { DuplicateDropDownMenuItem(onDuplicate = onDuplicate) }
    }
}
