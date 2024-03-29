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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme

@Composable
public fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = 150.dp),
    ) {
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewPrimaryButton() {
    KustomAlarmTheme {
        KaBackground {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PrimaryButton(text = "short", onClick = {})
                PrimaryButton(text = "medium text", onClick = {})
                PrimaryButton(text = "long text text text text text text", onClick = {})
                PrimaryButton(
                    text = "very long text text text text text text text text text text text text",
                    onClick = {},
                )
            }
        }
    }
}
