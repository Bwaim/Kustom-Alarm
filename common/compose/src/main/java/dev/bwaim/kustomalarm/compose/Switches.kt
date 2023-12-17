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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.icons.Schedule
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview

@Composable
public fun KaSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    var checkedInternal by remember {
        mutableStateOf(checked)
    }

    Switch(
        checked = checkedInternal,
        onCheckedChange = {
            checkedInternal = it
            onCheckedChange(it)
        },
        modifier = modifier,
        thumbContent = {
            if (checked) {
                Icon(imageVector = Icons.Filled.Schedule, contentDescription = null)
            }
        },
    )
}

@Composable
@PreviewsKAlarm
private fun PreviewKaSwitch() {
    KustomAlarmThemePreview {
        Column(modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth()) {
            KaSwitch()
            KaSwitch(checked = true)
        }
    }
}
