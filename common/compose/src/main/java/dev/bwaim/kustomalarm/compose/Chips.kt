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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview

@Composable
public fun KaFilterChip(
    label: @Composable () -> Unit,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        shape = MaterialTheme.shapes.extraLarge,
        border = null,
    )
}

@Composable
@PreviewsKAlarm
private fun KaFilterChipPreview() {
    KustomAlarmThemePreview {
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            KaFilterChip(label = { Text("LU") })
            Spacer(modifier = Modifier.height(5.dp))
            KaFilterChip(
                label = { Text("LU") },
                selected = true,
            )
        }
    }
}
