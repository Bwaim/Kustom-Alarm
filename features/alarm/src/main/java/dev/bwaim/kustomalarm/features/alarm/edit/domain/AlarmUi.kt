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

package dev.bwaim.kustomalarm.features.alarm.edit.domain

import android.content.Context
import android.media.RingtoneManager
import androidx.core.net.toUri
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import java.time.DayOfWeek
import java.time.LocalTime
import kotlin.time.Duration

internal data class AlarmUi(
    val id: Int = 0,
    val name: String?,
    val time: LocalTime,
    val weekDays: Set<DayOfWeek>,
    val isOnce: Boolean = false,
    val isActivated: Boolean = true,
    val uri: String,
    val ringtoneTitle: String,
    val postponeDuration: Duration,
)

internal fun Alarm.toAlarmUi(context: Context) =
    AlarmUi(
        id = id,
        name = name,
        time = time,
        weekDays = if (isOnce) emptySet() else weekDays,
        isOnce = isOnce,
        isActivated = isActivated,
        uri = uri,
        ringtoneTitle = RingtoneManager.getRingtone(context, uri.toUri()).getTitle(context),
        postponeDuration = postponeDuration,
    )

internal fun AlarmUi.toAlarm(): Alarm =
    Alarm(
        id = id,
        name = name,
        time = time,
        weekDays = weekDays,
        isOnce = weekDays.isEmpty(),
        isActivated = isActivated,
        uri = uri,
        postponeDuration = postponeDuration,
    )
