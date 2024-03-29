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
import dev.bwaim.kustomalarm.compose.PreviewDropDownMenuItem
import dev.bwaim.kustomalarm.compose.SetTemplateDropDownMenuItem

@Composable
internal fun AlarmMoreMenu(
    deleteAlarm: () -> Unit,
    setTemplate: () -> Unit,
    modifier: Modifier = Modifier,
    modifyAlarm: (() -> Unit)? = null,
    duplicateAlarm: (() -> Unit)? = null,
    preview: (() -> Unit)? = null,
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
            onDelete = deleteAlarm,
            onSetTemplate = {
                setTemplate()
                moreMenuExpanded = false
            },
            onPreview = {
                preview?.invoke()
                moreMenuExpanded = false
            },
            onModify = modifyAlarm,
            onDuplicate = duplicateAlarm,
        )
    }
}

@Composable
private fun EditDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    onSetTemplate: () -> Unit,
    onModify: (() -> Unit)? = null,
    onDuplicate: (() -> Unit)? = null,
    onPreview: (() -> Unit)? = null,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        onModify?.let { ModifyDropDownMenuItem(onModify = onModify) }
        onDuplicate?.let { DuplicateDropDownMenuItem(onDuplicate = onDuplicate) }
        SetTemplateDropDownMenuItem(onSetTemplate = onSetTemplate)
        onPreview?.let { PreviewDropDownMenuItem(onPreview = onPreview) }
        DeleteDropDownMenuItem(onDelete = onDelete)
    }
}
