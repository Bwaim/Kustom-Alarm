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

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.core.ApplicationScope
import dev.bwaim.kustomalarm.core.NotificationHelper
import dev.bwaim.kustomalarm.core.android.BuildWrapper
import dev.bwaim.kustomalarm.core.value
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.settings.SettingsService
import dev.bwaim.kustomalarm.settings.appstate.domain.NOT_SAVED_ALARM_ID
import dev.bwaim.kustomalarm.ui.resources.R.drawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

internal const val ID_RING_ALARM_EXTRA: String =
    "dev.bwaim.kustomalarm.features.alarm.ring.ID_RING_ALARM_EXTRA"
internal const val ALARM_SONG_EXTRA: String =
    "dev.bwaim.kustomalarm.features.alarm.ring.ALARM_SONG_EXTRA"

private const val RINGING_ALARM_NOTIFICATION_ID = 100001

@AndroidEntryPoint
public class RingingAlarmService @Inject constructor() : LifecycleService() {
    @Inject
    internal lateinit var alarmService: AlarmService

    @Inject
    internal lateinit var settingsService: SettingsService

    @Inject
    @ApplicationScope
    internal lateinit var applicationScope: CoroutineScope

    @Inject
    internal lateinit var notificationHelper: NotificationHelper

    private val notificationManager by lazy { getSystemService<NotificationManager>() }
    private val vibrator by lazy { getSystemService<Vibrator>() }

    private val mediaPlayer by lazy { MediaPlayer().also { it.isLooping = true } }

    private var alarmId: Int = NOT_SAVED_ALARM_ID

    override fun onDestroy() {
        applicationScope.launch {
            settingsService.setRingingAlarm(NOT_SAVED_ALARM_ID)
        }
        mediaPlayer.stop()
        vibrator?.cancel()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        // When the app is killed, we want to create the backstack when it's reopened from the notification
        if (alarmId != NOT_SAVED_ALARM_ID) {
            val notification = createRingingAlarmNotification(alarmId, withBackstack = true)
            notificationManager?.notify(RINGING_ALARM_NOTIFICATION_ID, notification)
        }

        super.onTaskRemoved(rootIntent)
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        alarmId =
            intent?.getIntExtra(ID_RING_ALARM_EXTRA, NOT_SAVED_ALARM_ID) ?: NOT_SAVED_ALARM_ID
        val uriParam = intent?.getStringExtra(ALARM_SONG_EXTRA)

        if (alarmId != NOT_SAVED_ALARM_ID) {
            getAlarm(alarmId = alarmId, uriParam = uriParam)
            val notification = createRingingAlarmNotification(alarmId)
            startForeground(RINGING_ALARM_NOTIFICATION_ID, notification)
        } else {
            stopSelf()
        }

        super.onStartCommand(intent, flags, startId)
        return START_REDELIVER_INTENT
    }

    private fun getAlarm(
        alarmId: Int,
        uriParam: String?,
    ) {
        lifecycleScope.launch {
            val alarm = alarmService.getAlarm(alarmId).value
            if (alarm != null) {
                startAlarm(alarm.copy(uri = uriParam?.let { uriParam } ?: alarm.uri))
            } else {
                stopSelf()
            }
        }
    }

    private fun startAlarm(alarm: Alarm) {
        mediaPlayer.setDataSource(this, alarm.uri.toUri())
        mediaPlayer.setAudioAttributes(buildAudioAttributes())
        mediaPlayer.setOnPreparedListener {
            alarmVolumeVariations()
        }
        mediaPlayer.prepareAsync()

        val intent =
            RingActivity.createIntent(
                context = this,
                alarmId = alarm.id,
                flags = Intent.FLAG_ACTIVITY_NEW_TASK,
            )
        startActivity(intent)

        setVibration()
    }

    private fun buildAudioAttributes(): AudioAttributes =
        AudioAttributes
            .Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

    @Suppress("MagicNumber")
    private fun alarmVolumeVariations() {
        var volume = 0.01f
        mediaPlayer.setVolume(volume)
        mediaPlayer.start()
        lifecycleScope.launch {
            delay(5000)
            volume = 0.05f
            mediaPlayer.setVolume(volume)
            repeat(10) {
                delay(1000)
                volume += 0.01f
                mediaPlayer.setVolume(volume)
            }
        }
    }

    private fun MediaPlayer.setVolume(volume: Float) {
        setVolume(volume, volume)
    }

    @Suppress("MagicNumber")
    private fun setVibration() {
        if (BuildWrapper.isAtLeastO) {
            val timings: LongArray = longArrayOf(150, 200, 150, 500)
            val amplitudes: IntArray = intArrayOf(64, 128, 64, 0)
            val repeat = 0
            val repeatingEffect = VibrationEffect.createWaveform(timings, amplitudes, repeat)
            vibrator?.vibrate(repeatingEffect)
        }
    }

    private fun createRingingAlarmNotification(
        alarmId: Int,
        withBackstack: Boolean = false,
    ): Notification {
        val builder =
            NotificationCompat
                .Builder(
                    this,
                    notificationHelper.getAlarmNotificationChannelId(),
                ).setSmallIcon(drawable.ic_notification_klock)
                .setContentTitle(getString(string.notification_firing_alarm_title))
                .setContentText(getString(string.notification_firing_alarm_description))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(null)
                .setContentIntent(
                    RingActivity.createPendingIntent(
                        context = this@RingingAlarmService,
                        alarmId = alarmId,
                        withBackstack = withBackstack,
                    ),
                )

        val notification = builder.build()
        notification.flags = notification.flags or NotificationCompat.FLAG_NO_CLEAR

        return notification
    }

    public companion object {
        public fun createIntent(
            context: Context,
            alarmId: Int,
            alarmUri: String?,
        ): Intent =
            Intent(context, RingingAlarmService::class.java).apply {
                putExtra(ID_RING_ALARM_EXTRA, alarmId)
                putExtra(ALARM_SONG_EXTRA, alarmUri)
            }
    }
}
