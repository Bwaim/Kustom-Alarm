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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.localisation.R.string

@Composable
public fun KaErrorMessage(
    errorMessage: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    Text(
        text = errorMessage,
        modifier = modifier,
        textAlign = textAlign,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
public fun KaCloseErrorMessage(
    errorMessage: String,
    close: () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = TextAlign.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    spacerHeight: Dp = 20.dp,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
    ) {
        KaErrorMessage(
            errorMessage = errorMessage,
            textAlign = textAlign,
        )
        Spacer(modifier = Modifier.height(spacerHeight))
        PrimaryButton(
            text = stringResource(id = string.navigation_close_content_description),
            onClick = close,
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewKaErrorMessage() {
    KustomAlarmThemePreview {
        KaErrorMessage(
            errorMessage = "Preview error message",
            modifier =
                Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewKaCloseErrorMessage() {
    KustomAlarmThemePreview {
        KaCloseErrorMessage(
            errorMessage = "Preview close error message",
            modifier =
                Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
            close = {},
        )
    }
}
