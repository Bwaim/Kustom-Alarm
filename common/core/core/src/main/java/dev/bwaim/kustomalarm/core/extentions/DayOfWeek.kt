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

package dev.bwaim.kustomalarm.core.extentions

import java.time.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.format.TextStyle.FULL
import java.time.format.TextStyle.SHORT
import java.util.Locale

public fun DayOfWeek.shortName(locale: Locale): String {
    return getDisplayName(SHORT, locale).substring(startIndex = 0, endIndex = 2).uppercase()
}

public fun DayOfWeek.longName(locale: Locale): String {
    return getDisplayName(FULL, locale).uppercase()
}

public fun Set<DayOfWeek>.areWeekendDays(): Boolean {
    return size == 2 && this.containsAll(setOf(SATURDAY, SUNDAY))
}

public fun Set<DayOfWeek>.areWeekDays(): Boolean {
    return size == NUM_WORKING_DAYS && this.containsAll(setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY))
}

private const val NUM_WORKING_DAYS = 5
