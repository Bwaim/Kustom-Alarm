/*
 * Copyright 2024 Dev Bwaim team
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

package dev.bwaim.kustomalarm.features.alarm.ring.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.localisation.R.string

@Composable
internal fun RingScreen(
    currentTime: String,
    snoozedTime: String?,
    name: String?,
    turnOff: () -> Unit,
    postponeAlarm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        turnOff()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Timer(currentTime)

        AlarmName(name)

        Spacer(modifier = Modifier.weight(1f))

        if (snoozedTime == null) {
            PostponeCard(postponeAlarm)
        } else {
            Timer(snoozedTime)
        }

        TurnOffCard(turnOff)
    }
}

@Composable
private fun Timer(currentTime: String) {
    Text(
        text = currentTime,
        fontSize = 80.sp,
        modifier = Modifier.padding(top = 150.dp),
    )
}

@Composable
private fun AlarmName(name: String?) {
    name?.let {
        Text(
            text = name,
        )
    }
}

@Composable
private fun PostponeCard(postponeAlarm: () -> Unit) {
    Card(
        onClick = postponeAlarm,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(200.dp),
    ) {
        Text(
            text = stringResource(id = string.ring_screen_snooze),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically),
        )
    }
}

@Composable
private fun TurnOffCard(turnOff: () -> Unit) {
    Card(
        onClick = turnOff,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        modifier = Modifier
            .padding(top = 20.dp, bottom = 16.dp)
            .fillMaxWidth(0.8f)
            .height(70.dp),
    ) {
        Text(
            text = stringResource(id = string.ring_screen_turn_off),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically),
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewRingScreen() {
    KustomAlarmThemePreview {
        RingScreen(
            currentTime = "09:00",
            snoozedTime = null,
            name = "alarm name",
            turnOff = {},
            postponeAlarm = {},
        )
    }
}
