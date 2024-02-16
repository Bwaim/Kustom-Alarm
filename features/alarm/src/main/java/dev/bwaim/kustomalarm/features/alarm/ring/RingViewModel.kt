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

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.analytics.AnalyticsService
import dev.bwaim.kustomalarm.core.ApplicationScope
import dev.bwaim.kustomalarm.core.NotificationHelper
import dev.bwaim.kustomalarm.core.value
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.settings.SettingsService
import dev.bwaim.kustomalarm.settings.appstate.domain.DEFAULT_RINGING_ALARM
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import dev.bwaim.kustomalarm.ui.resources.R.drawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle.SHORT
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timer

internal const val RINGING_ALARM_NOTIFICATION_ID = 100001

@HiltViewModel
internal class RingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val appContext: Context,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val settingsService: SettingsService,
    private val analyticsService: AnalyticsService,
    private val alarmService: AlarmService,
    private val notificationHelper: NotificationHelper,
) : ViewModel() {
    private val args = RingArgs(savedStateHandle)
    private val alarmId = args.id

    @SuppressLint("StaticFieldLeak")
    private val context = ContextCompat.getContextForLanguage(appContext)

    private val _alarm: MutableStateFlow<Alarm?> = MutableStateFlow(null)
    val alarm: StateFlow<Alarm?> = _alarm.asStateFlow()

    val selectedTheme: StateFlow<Theme?> =
        settingsService
            .observeTheme()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null,
            )

    val currentTime: MutableStateFlow<String> = MutableStateFlow(getTime())

    private lateinit var timer: Timer

    private var ringtone: Ringtone? = null

    private val notificationManager = appContext.getSystemService<NotificationManager>()

    init {
        viewModelScope.launch {
            timer =
                timer(period = 300) {
                    currentTime.value = getTime()
                }

            getAlarm()
        }

        _alarm
            .filterNotNull()
            .onEach {
                ringtone = getRingtone(it.uri)
                createRingingAlarmNotification()
                settingsService.setRingingAlarm(alarmId)
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        turnOffAlarm()
    }

    fun logScreenView(
        screenName: String,
        screenClass: String,
    ) {
        viewModelScope.launch {
            analyticsService.logScreenView(screenName = screenName, screenClass = screenClass)
        }
    }

    private fun getTime(): String {
        val localizedTimeFormatter = DateTimeFormatter.ofLocalizedTime(SHORT)
        return LocalTime.now().format(localizedTimeFormatter)
    }

    private suspend fun getAlarm() {
        _alarm.update { alarmService.getAlarm(alarmId).value }
    }

    private fun getRingtone(uri: String): Ringtone {
        return RingtoneManager.getRingtone(appContext, uri.toUri())
            .apply { buildAudioAttributes() }
    }

    private fun Ringtone.buildAudioAttributes() {
        audioAttributes =
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
    }

    private fun turnOffAlarm() {
        timer.cancel()
        applicationScope.launch {
            notificationManager?.cancel(RINGING_ALARM_NOTIFICATION_ID)
            settingsService.setRingingAlarm(DEFAULT_RINGING_ALARM)
        }
    }

    private fun createRingingAlarmNotification() {
        viewModelScope.launch {
            if (hasNotificationRights()) {
                val builder = NotificationCompat.Builder(
                    appContext,
                    notificationHelper.getAlarmNotificationChannelId(),
                )
                    .setSmallIcon(drawable.ic_notification_klock)
                    .setContentTitle(context.getString(string.notification_firing_alarm_title))
                    .setContentText(context.getString(string.notification_firing_alarm_description))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setContentIntent(RingActivity.createPendingIntent(appContext, alarmId))

                val notification = builder.build()
                notification.flags = notification.flags or NotificationCompat.FLAG_NO_CLEAR

                notificationManager?.notify(RINGING_ALARM_NOTIFICATION_ID, notification)
            }
        }
    }

    private fun hasNotificationRights(): Boolean {
        return notificationManager?.areNotificationsEnabled() ?: false
    }
}

private class RingArgs(val id: Int) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            id = checkNotNull(savedStateHandle.get<Int>(ID_RING_ACTIVITY_ARG)),
        )
}
