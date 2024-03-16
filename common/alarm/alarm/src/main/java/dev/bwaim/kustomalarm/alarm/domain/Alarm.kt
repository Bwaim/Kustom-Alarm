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

package dev.bwaim.kustomalarm.alarm.domain

import java.time.DayOfWeek
import java.time.LocalTime

public const val TEMPORAL_ALARM_ID: Int = -1

public data class Alarm(
    val id: Int = 0,
    val name: String?,
    val time: LocalTime,
    val weekDays: Set<DayOfWeek>,
    val isOnce: Boolean = false,
    val isActivated: Boolean = true,
    val uri: String,
)
