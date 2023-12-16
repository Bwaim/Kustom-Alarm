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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview

@Composable
public fun KaFilterChip(
    label: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier,
    initialSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    var selected by remember { mutableStateOf(initialSelected) }

    val color =
        if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            Color.Transparent
        }

    Surface(
        selected = selected,
        onClick = {
            selected = !selected
            onClick()
        },
        modifier = modifier.semantics { role = Role.Checkbox },
        shape = MaterialTheme.shapes.extraLarge,
        color = color,
        border = null,
    ) {
        label(Modifier.padding(8.dp))
    }
}

@Composable
@PreviewsKAlarm
private fun KaFilterChipPreview() {
    KustomAlarmThemePreview {
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            KaFilterChip(
                label = {
                    Text(
                        modifier = it,
                        text = "LU",
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            KaFilterChip(
                label = {
                    Text(
                        modifier = it,
                        text = "LU",
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                initialSelected = true,
            )
        }
    }
}
