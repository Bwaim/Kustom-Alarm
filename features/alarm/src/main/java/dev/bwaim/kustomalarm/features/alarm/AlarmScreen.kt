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

package dev.bwaim.kustomalarm.features.alarm

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.bwaim.kustomalarm.compose.Header
import dev.bwaim.kustomalarm.compose.KAlarmPreviews
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.ui.resources.R.drawable

@Composable
public fun AlarmRoute() {
    AlarmScreen()
}

@Composable
private fun AlarmScreen() {
    Header(
        title = stringResource(id = string.alarm_screen_titre),
        imageRes = drawable.alarm_background,
    )
}

@Composable
@KAlarmPreviews
private fun PreviewAlarmScreen() {
    KustomAlarmTheme {
        Surface {
            AlarmScreen()
        }
    }
}
