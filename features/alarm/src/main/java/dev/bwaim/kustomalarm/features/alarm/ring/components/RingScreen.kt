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

@file:OptIn(ExperimentalAnimationGraphicsApi::class)

package dev.bwaim.kustomalarm.features.alarm.ring.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.ui.resources.R.drawable
import kotlinx.coroutines.delay

@Composable
internal fun RingScreen(
    currentTime: String,
    snoozedTime: String?,
    name: String?,
    turnOff: () -> Unit,
    snoozeAlarm: () -> Unit,
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
            SnoozeCard(snoozeAlarm)
        } else {
            SnoozedInfo(snoozedTime)
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
private fun SnoozeCard(snoozeAlarm: () -> Unit) {
    Card(
        onClick = snoozeAlarm,
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
private fun ColumnScope.SnoozedInfo(snoozedTime: String) {
    SleepAnimation()
    Text(
        text = snoozedTime,
        fontSize = 40.sp,
        modifier = Modifier.padding(top = 50.dp),
    )
    Text(
        text = stringResource(id = string.ring_screen_snoozed),
    )
}

@Composable
private fun SleepAnimation() {
    val image = AnimatedImageVector.animatedVectorResource(drawable.avd_z_anim)
    var atEnd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            atEnd = !atEnd
            delay(1000) // delay between animations
        }
    }

    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd),
        contentDescription = null,
        modifier = Modifier.scale(2f),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiaryContainer),
    )
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
            snoozeAlarm = {},
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewSnoozedRingScreen() {
    KustomAlarmThemePreview {
        RingScreen(
            currentTime = "09:00",
            snoozedTime = "0:04:50",
            name = "alarm name",
            turnOff = {},
            snoozeAlarm = {},
        )
    }
}
