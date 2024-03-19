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

package dev.bwaim.kustomalarm.features.alarm.ring

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.core.NotificationHelper
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.settings.appstate.domain.NOT_SAVED_ALARM_ID
import dev.bwaim.kustomalarm.ui.resources.R.drawable
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal const val ID_SNOOZED_ALARM_EXTRA: String =
    "dev.bwaim.kustomalarm.features.alarm.ring.ID_SNOOZED_ALARM_EXTRA"
internal const val SNOOZED_TIME_EXTRA: String =
    "dev.bwaim.kustomalarm.features.alarm.ring.SNOOZED_TIME_EXTRA"
internal const val WITH_BACKSTACK_EXTRA: String =
    "dev.bwaim.kustomalarm.features.alarm.ring.WITH_BACKSTACK_EXTRA"

private const val SNOOZED_ALARM_NOTIFICATION_ID = 100001

@AndroidEntryPoint
internal class SnoozedAlarmService @Inject constructor() : LifecycleService() {
    @Inject
    internal lateinit var alarmService: AlarmService

    @Inject
    internal lateinit var notificationHelper: NotificationHelper

    private val notificationManager by lazy { getSystemService<NotificationManager>() }

    private var alarmId = NOT_SAVED_ALARM_ID
    private var snoozedTime = ""

    override fun onTaskRemoved(rootIntent: Intent?) {
        // When the app is killed, we want to create the backstack when it's reopened from the notification
        if (alarmId != NOT_SAVED_ALARM_ID) {
            createSnoozeAlarmNotification(
                alarmId = alarmId,
                snoozedTime = snoozedTime,
                withBackstack = true,
            )
        }

        super.onTaskRemoved(rootIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }

        alarmId = intent.getIntExtra(ID_SNOOZED_ALARM_EXTRA, -1)
        snoozedTime = intent.getStringExtra(SNOOZED_TIME_EXTRA) ?: ""
        val withBackstack = intent.getBooleanExtra(WITH_BACKSTACK_EXTRA, false)

        createSnoozeAlarmNotification(
            alarmId = alarmId,
            snoozedTime = snoozedTime,
            withBackstack = withBackstack,
        )
        observeSnoozeEnd()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun observeSnoozeEnd() {
        alarmService
            .observeSnoozedAlarm()
            .filter { it == null }
            .onEach {
                notificationManager?.cancel(SNOOZED_ALARM_NOTIFICATION_ID)
                stopSelf()
            }
            .launchIn(lifecycleScope)
    }

    private fun createSnoozeAlarmNotification(
        alarmId: Int,
        snoozedTime: String,
        withBackstack: Boolean,
    ) {
        val builder = NotificationCompat.Builder(
            this,
            notificationHelper.getAlarmNotificationChannelId(),
        )
            .setSmallIcon(drawable.ic_notification_klock)
            .setContentTitle(getString(string.notification_snooze_alarm_title))
            .setContentText(getString(string.notification_snooze_alarm_description, snoozedTime))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(null)
            .setContentIntent(
                RingActivity.createPendingIntent(
                    context = this,
                    alarmId = alarmId,
                    withBackstack = withBackstack,
                ),
            )

        val notification = builder.build()
        notification.flags = notification.flags or NotificationCompat.FLAG_NO_CLEAR

        notificationManager?.notify(SNOOZED_ALARM_NOTIFICATION_ID, notification)
    }

    companion object {
        fun createIntent(
            context: Context,
            alarmId: Int,
            snoozedTime: String?,
            withBackstack: Boolean,
        ): Intent {
            return Intent(context, SnoozedAlarmService::class.java).apply {
                putExtra(ID_SNOOZED_ALARM_EXTRA, alarmId)
                putExtra(SNOOZED_TIME_EXTRA, snoozedTime)
                putExtra(WITH_BACKSTACK_EXTRA, withBackstack)
            }
        }
    }
}
