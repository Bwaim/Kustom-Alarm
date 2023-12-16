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

package dev.bwaim.kustomalarm.core.android.extensions

import android.content.Context
import android.icu.text.MeasureFormat
import android.icu.util.MeasureUnit
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import dev.bwaim.kustomalarm.core.android.BuildWrapper
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

public fun Context.getAppLocale(): Locale {
    val localeList = AppCompatDelegate.getApplicationLocales()
    return if (localeList.isEmpty) {
        getPhoneLocale()
    } else {
        localeList.get(0) ?: getPhoneLocale()
    }
}

public fun Context.getPhoneLocale(): Locale {
    val localeList = resources.configuration.locales
    return if (localeList.isEmpty) {
        Locale.ENGLISH
    } else {
        localeList.get(0)
    }
}

public fun Context.toast(
    text: String,
    duration: Int = Toast.LENGTH_LONG,
) {
    Toast.makeText(this, text, duration).show()
}

public fun Context.localizedHour(): String {
    return if (BuildWrapper.isAtLeastP) {
        localizedUnit(MeasureUnit.HOUR)
    } else {
        "h"
    }
}

public fun Context.localizedMinute(): String {
    return if (BuildWrapper.isAtLeastP) {
        localizedUnit(MeasureUnit.MINUTE)
    } else {
        "m"
    }
}

public fun Context.formatDuration(duration: Duration): String {
    val hours = duration.inWholeHours
    val minutes = duration.minus(hours.hours).inWholeMinutes

    val stringDuration =
        listOf(
            hours to localizedHour(),
            minutes to localizedMinute(),
        )
            .filter { it.first > 0 }
            .joinToString(separator = " ") { "${it.first} ${it.second}" }

    return stringDuration.ifBlank { "0 ${localizedMinute()}" }
}

@RequiresApi(Build.VERSION_CODES.P)
private fun Context.localizedUnit(unit: MeasureUnit): String {
    val measureFormat = MeasureFormat.getInstance(getAppLocale(), MeasureFormat.FormatWidth.NARROW)
    return measureFormat.getUnitDisplayName(unit)
}
