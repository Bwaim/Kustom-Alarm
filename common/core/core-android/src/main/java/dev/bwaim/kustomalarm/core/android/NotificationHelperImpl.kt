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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.core.NotificationHelper
import dev.bwaim.kustomalarm.localisation.R.string
import javax.inject.Inject

private const val FIRING_ALARM_NOTIFICATION_CHANNEL = "firing_alarms"

internal class NotificationHelperImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : NotificationHelper {
        private val notificationManager: NotificationManager =
            context.getSystemService<NotificationManager>() ?: throw IllegalStateException()

        override fun setUpNotificationChannels() {
            if (BuildWrapper.isAtLeastO) {
                createAlarmNotificationChannel()
            }
        }

        @RequiresApi(VERSION_CODES.O)
        private fun createAlarmNotificationChannel() {
            if (notificationManager.getNotificationChannel(FIRING_ALARM_NOTIFICATION_CHANNEL) == null) {
                val name = context.getString(string.notification_channel_alarms_name)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel =
                    NotificationChannel(
                        // id =
                        FIRING_ALARM_NOTIFICATION_CHANNEL,
                        // name =
                        name,
                        // importance =
                        importance,
                    )
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
