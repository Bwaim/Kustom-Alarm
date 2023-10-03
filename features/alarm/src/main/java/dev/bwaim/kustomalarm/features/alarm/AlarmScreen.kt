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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.Header
import dev.bwaim.kustomalarm.compose.KAlarmPreviews
import dev.bwaim.kustomalarm.compose.KaBackground
import dev.bwaim.kustomalarm.compose.KaCenterAlignedTopAppBar
import dev.bwaim.kustomalarm.compose.PrimaryButton
import dev.bwaim.kustomalarm.compose.SurfaceCard
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.ui.resources.R.drawable

@Composable
public fun AlarmRoute(openDrawer: () -> Unit) {
    AlarmScreen(
        openDrawer = openDrawer,
    )
}

@Composable
private fun AlarmScreen(
    openDrawer: () -> Unit,
) {
    Scaffold(
        topBar = {
            KaCenterAlignedTopAppBar(
                onClickNavigation = openDrawer,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SurfaceCard {
                Header(
                    title = stringResource(id = string.alarm_screen_titre),
                    imageRes = drawable.alarm_background,
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            NoAlarm()
        }
    }
}

@Composable
private fun ColumnScope.NoAlarm() {
    Text(
        text = stringResource(id = string.alarm_screen_no_alarm_msg),
        style = MaterialTheme.typography.headlineSmall,
    )
    Spacer(modifier = Modifier.height(60.dp))
    PrimaryButton(
        text = stringResource(id = string.alarm_screen_no_alarm_add_button),
        onClick = {},
    )
}

@Composable
@KAlarmPreviews
private fun PreviewAlarmScreen() {
    KustomAlarmTheme {
        KaBackground {
            AlarmScreen(
                openDrawer = {},
            )
        }
    }
}