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

package dev.bwaim.kustomalarm.core.extentions

import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

public fun Duration.toMinutesSeconds(): String {
    val minutes = inWholeMinutes
    val seconds = minus(minutes.minutes).inWholeSeconds
    return "%02d:%02d".format(minutes, seconds)
}

public fun Duration.toHoursMinutesSeconds(): String {
    val hours = inWholeHours
    var remaining = minus(hours.hours)
    val minutes = remaining.inWholeMinutes
    remaining = remaining.minus(minutes.minutes)
    val seconds = remaining.inWholeSeconds
    return "%01d:%02d:%02d".format(hours, minutes, seconds)
}
