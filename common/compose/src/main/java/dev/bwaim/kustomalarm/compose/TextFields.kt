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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview

@Composable
public fun KaLargeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    KaTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = MaterialTheme.typography.headlineLarge,
        label = label?.let {
            {
                Text(
                    text = label,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        },
        interactionSource = interactionSource,
    )
}

@Composable
public fun KaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        label = label,
        interactionSource = interactionSource,
    )
}

@Composable
@KAlarmPreviews
private fun KaTextFieldPreview() {
    KustomAlarmTheme {
        KaTextField(value = "textField value", onValueChange = {})
    }
}

@Composable
@DarkLightPreviews
private fun GlobalKaTextFieldPreview() {
    KustomAlarmThemePreview {
        Column(modifier = Modifier.padding(10.dp)) {
            KaLargeTextField(
                value = "",
                onValueChange = {},
                label = "label",
            )

            Spacer(modifier = Modifier.height(10.dp))

            KaLargeTextField(
                value = "textField value",
                onValueChange = {},
                label = "label",
            )
        }
    }
}
