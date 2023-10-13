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

package dev.bwaim.kustomalarm.features.alarm.edit

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import dev.bwaim.kustomalarm.compose.KAlarmPreviews
import dev.bwaim.kustomalarm.compose.KaCloseTopAppBar
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview

@Composable
internal fun EditAlarmRoute(
    close: () -> Unit,
) {
    EditAlarmScreen(
        close = close,
    )
}

@Composable
private fun EditAlarmScreen(
    close: () -> Unit,
) {
    Scaffold(
        topBar = {
            KaCloseTopAppBar(
                onClickNavigation = close,
            )
        },
    ) { padding ->
    }
}

@Composable
@KAlarmPreviews
private fun PreviewEditAlarmScreen() {
    KustomAlarmThemePreview {
        EditAlarmScreen(
            close = {},
        )
    }
}
