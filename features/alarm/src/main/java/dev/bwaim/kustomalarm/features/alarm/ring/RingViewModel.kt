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
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.analytics.AnalyticsService
import dev.bwaim.kustomalarm.core.ApplicationScope
import dev.bwaim.kustomalarm.core.extentions.toHoursMinutesSeconds
import dev.bwaim.kustomalarm.core.value
import dev.bwaim.kustomalarm.settings.SettingsService
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle.SHORT
import java.time.temporal.ChronoUnit
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timer
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val TIMER = 300L

@HiltViewModel
internal class RingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val appContext: Context,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val settingsService: SettingsService,
    private val analyticsService: AnalyticsService,
    private val alarmService: AlarmService,
) : ViewModel() {
    private val args = RingArgs(savedStateHandle)
    private val alarmId = args.id

    private val alarmManager by lazy { appContext.getSystemService<AlarmManager>() }

    private val _alarm: MutableStateFlow<Alarm?> = MutableStateFlow(null)
    val alarm: StateFlow<Alarm?> = _alarm.asStateFlow()

    private val snoozedDuration: MutableStateFlow<Duration?> = MutableStateFlow(null)

    val selectedTheme: StateFlow<Theme?> =
        settingsService
            .observeTheme()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null,
            )

    val currentTime: MutableStateFlow<String> = MutableStateFlow(getTime())
    val snoozedTime: MutableStateFlow<String?> = MutableStateFlow(null)

    private lateinit var timer: Timer

    init {
        viewModelScope.launch {
            timer =
                timer(period = TIMER) {
                    currentTime.value = getTime()
                    snoozedTime.value = snoozedTime()
                    snoozedDuration.update { duration ->
                        duration?.let {
                            val newDuration = duration.minus(TIMER.milliseconds)
                            if (newDuration.inWholeMilliseconds <= 0) {
                                null
                            } else {
                                newDuration
                            }
                        }
                    }
                }

            getAlarm()
        }
    }

    fun logScreenView(
        screenName: String,
        screenClass: KClass<*>,
    ) {
        viewModelScope.launch {
            analyticsService.logScreenView(
                screenName = screenName,
                screenClass = screenClass::class.java.simpleName,
            )
        }
    }

    private fun getTime(localTime: LocalTime = LocalTime.now()): String {
        val localizedTimeFormatter = DateTimeFormatter.ofLocalizedTime(SHORT)
        return localTime.format(localizedTimeFormatter)
    }

    private fun snoozedTime(): String? = snoozedDuration.value?.toHoursMinutesSeconds()

    private suspend fun getAlarm() {
        _alarm.update {
            alarmService
                .getAlarm(alarmId)
                .value
                .also { alarm ->
                    alarm?.let {
                        if (it.postponeTime == null) {
                            // Alarm is ringing
                            settingsService.setRingingAlarm(alarmId)
                        } else {
                            snoozedDuration.value = computeRemainingSnoozeDuration(it)
                            snoozedDuration.value?.let { duration ->
                                val snoozedTime =
                                    LocalTime.now().plusSeconds(duration.inWholeSeconds)
                                startSnoozeService(
                                    snoozedTime = getTime(snoozedTime),
                                    withBackstack = false,
                                )
                            }
                        }
                    }
                }
        }
    }

    fun turnOffAlarm() {
        timer.cancel()
        applicationScope.launch {
            stopAlarmService()
            alarm.value?.let { alarmService.saveAlarm(it.copy(postponeTime = null)) }
            val pendingIntent = createSnoozeIntent()
            alarmManager?.cancel(pendingIntent)
        }
    }

    fun snoozeAlarm() {
        viewModelScope.launch {
            stopAlarmService()
            alarm.value?.let {
                scheduleAlarm(it.postponeDuration)
                val snoozedAt = LocalTime.now()
                alarmService.saveAlarm(it.copy(postponeTime = snoozedAt))
                snoozedDuration.value = it.postponeDuration
            }
        }
    }

    private fun stopAlarmService() {
        val intent = Intent(appContext, RingingAlarmService::class.java)
        appContext.stopService(intent)
    }

    @SuppressLint("MissingPermission")
    private fun scheduleAlarm(delay: Duration) {
        val triggerTime = System.currentTimeMillis() + delay.inWholeMilliseconds
        val snoozedTime = LocalTime.now().plusMinutes(delay.inWholeMinutes)

        val pendingIntent = createSnoozeIntent()

        val alarmClockInfo =
            AlarmManager.AlarmClockInfo(
                // triggerTime =
                triggerTime,
                // showIntent =
                pendingIntent,
            )
        alarmManager?.setAlarmClock(
            // info =
            alarmClockInfo,
            // operation =
            pendingIntent,
        )

        startSnoozeService(snoozedTime = getTime(snoozedTime))
    }

    private fun createSnoozeIntent(): PendingIntent {
        val intent = AlarmBroadcastReceiver.createIntent(appContext, alarmId)
        return PendingIntent.getBroadcast(
            // context =
            appContext,
            // requestCode =
            alarmId,
            // intent =
            intent,
            // flags =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun computeRemainingSnoozeDuration(alarm: Alarm): Duration? {
        val now = LocalTime.now()
        val snoozeTime = alarm.postponeTime ?: now
        val initialSnoozeDuration = alarm.postponeDuration
        val elapsedDuration = snoozeTime.until(now, ChronoUnit.MILLIS).milliseconds
        val remainingSnoozeDuration = initialSnoozeDuration - elapsedDuration
        return if (remainingSnoozeDuration.inWholeMilliseconds <= 0) {
            null
        } else {
            remainingSnoozeDuration
        }
    }

    private fun startSnoozeService(
        snoozedTime: String,
        withBackstack: Boolean = args.withBackstack,
    ) {
        val intent =
            SnoozedAlarmService.createIntent(
                context = appContext,
                alarmId = alarmId,
                snoozedTime = snoozedTime,
                withBackstack = withBackstack,
            )
        appContext.startService(intent)
    }
}

private class RingArgs(
    val id: Int,
    val withBackstack: Boolean,
) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            id = checkNotNull(savedStateHandle.get<Int>(ID_RING_ALARM_ARG)),
            withBackstack = checkNotNull(savedStateHandle.get<Boolean>(WITH_BACKSTACK_ARG)),
        )
}
