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

package dev.bwaim.kustomalarm.core.android

import android.content.Context
import android.media.RingtoneManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.core.RingtoneUtils
import dev.bwaim.kustomalarm.core.android.extensions.toRingtoneUri
import javax.inject.Inject

internal class RingtoneUtilsImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : RingtoneUtils {
    override fun getDefaultRingtoneUri(): String {
        val ringtoneManager =
            RingtoneManager(context).also {
                it.setType(RingtoneManager.TYPE_ALARM or RingtoneManager.TYPE_RINGTONE)
            }
        val cursor = ringtoneManager.cursor
        return if (cursor.moveToNext()) {
            cursor.toRingtoneUri()
        } else {
            ""
        }
    }
}
