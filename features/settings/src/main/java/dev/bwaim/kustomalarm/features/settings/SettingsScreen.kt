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

package dev.bwaim.kustomalarm.features.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.bwaim.kustomalarm.compose.KAlarmPreviews
import dev.bwaim.kustomalarm.compose.KaBackTopAppBar
import dev.bwaim.kustomalarm.compose.KaBackground
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.localisation.R.string

@Composable
internal fun SettingsRoute(
    onClose: () -> Unit,
) {
    SettingsScreen(onClose = onClose)
}

@Composable
private fun SettingsScreen(
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            KaBackTopAppBar(
                onClickNavigation = onClose,
                title = {
                    Text(text = stringResource(id = string.settings_screen_label))
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
        ) {
        }
    }
}

@Composable
@KAlarmPreviews
private fun PreviewSettingsScreen() {
    KustomAlarmTheme {
        KaBackground {
            SettingsScreen(
                onClose = {},
            )
        }
    }
}
