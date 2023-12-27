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

package dev.bwaim.kustomalarm.features.alarm.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.compose.KaSwitch
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.core.extentions.areWeekDays
import dev.bwaim.kustomalarm.core.extentions.areWeekendDays
import dev.bwaim.kustomalarm.core.extentions.longName
import dev.bwaim.kustomalarm.core.extentions.shortName
import dev.bwaim.kustomalarm.features.alarm.edit.components.AlarmMoreMenu
import dev.bwaim.kustomalarm.localisation.R.string
import java.time.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.LocalTime
import java.util.Locale

@Composable
public fun AlarmRow(
    alarm: Alarm,
    updateAlarm: (Alarm) -> Unit,
    deleteAlarm: () -> Unit,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.ENGLISH,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KaSwitch(
            checked = alarm.isActivated,
            onCheckedChange = { activated -> updateAlarm(alarm.copy(isActivated = activated)) },
        )

        Column(
            modifier = Modifier.padding(horizontal = 15.dp),
        ) {
            Text(
                text = alarm.time.toString(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text =
                    alarm.weekDays.toLabel(
                        context = LocalContext.current,
                        locale = locale,
                        time = alarm.time,
                        isOnce = alarm.isOnce,
                    ).uppercase(),
                style = MaterialTheme.typography.labelSmall,
            )
        }

        alarm.name?.let {
            Text(
                text = it,
                modifier = Modifier.weight(10f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        AlarmMoreMenu(
            deleteAlarm = deleteAlarm,
        )
    }
}

private fun Set<DayOfWeek>.toLabel(
    context: Context,
    locale: Locale,
    time: LocalTime,
    isOnce: Boolean,
): String {
    return when {
        isOnce -> {
            val now = LocalTime.now()
            if (time.isAfter(now)) {
                context.getString(string.global_today)
            } else {
                context.getString(string.global_tomorrow)
            }
        }

        size == 1 -> {
            this.joinToString { it.longName(locale) }
        }

        areWeekendDays() -> {
            context.getString(string.alarm_screen_alarms_days_weekend)
        }

        areWeekDays() -> {
            context.getString(string.alarm_screen_alarms_days_week)
        }

        size == 7 -> {
            context.getString(string.alarm_screen_alarms_days_all)
        }

        else -> {
            this.joinToString(separator = ", ") { it.shortName(locale) }
        }
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewAlarmRow() {
    KustomAlarmThemePreview {
        LazyColumn {
            item {
                AlarmRow(
                    alarm =
                        Alarm(
                            id = 1,
                            name = "alarm1",
                            time = LocalTime.of(7, 35),
                            weekDays = setOf(MONDAY, TUESDAY),
                            isOnce = false,
                            isActivated = false,
                        ),
                    updateAlarm = {},
                    deleteAlarm = {},
                )
            }

            item {
                AlarmRow(
                    alarm =
                        Alarm(
                            id = 1,
                            name = "long long long long long long long long",
                            time = LocalTime.of(8, 35),
                            weekDays = setOf(),
                            isOnce = true,
                        ),
                    updateAlarm = {},
                    deleteAlarm = {},
                )
            }

            item {
                AlarmRow(
                    alarm =
                        Alarm(
                            id = 1,
                            name = null,
                            time = LocalTime.of(9, 35),
                            weekDays = setOf(WEDNESDAY),
                            isOnce = false,
                        ),
                    updateAlarm = {},
                    deleteAlarm = {},
                )
            }

            item {
                AlarmRow(
                    alarm =
                        Alarm(
                            id = 1,
                            name = null,
                            time = LocalTime.of(11, 35),
                            weekDays = setOf(SATURDAY, SUNDAY),
                            isOnce = false,
                        ),
                    updateAlarm = {},
                    deleteAlarm = {},
                )
            }

            item {
                AlarmRow(
                    alarm =
                        Alarm(
                            id = 1,
                            name = null,
                            time = LocalTime.of(12, 35),
                            weekDays = setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY),
                            isOnce = false,
                        ),
                    updateAlarm = {},
                    deleteAlarm = {},
                )
            }

            item {
                AlarmRow(
                    alarm =
                        Alarm(
                            id = 1,
                            name = null,
                            time = LocalTime.of(13, 35),
                            weekDays =
                                setOf(
                                    MONDAY,
                                    TUESDAY,
                                    WEDNESDAY,
                                    THURSDAY,
                                    FRIDAY,
                                    SATURDAY,
                                    SUNDAY,
                                ),
                            isOnce = false,
                        ),
                    updateAlarm = {},
                    deleteAlarm = {},
                )
            }
        }
    }
}
