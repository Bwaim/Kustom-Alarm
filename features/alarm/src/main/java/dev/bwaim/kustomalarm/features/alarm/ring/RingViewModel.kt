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

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.analytics.AnalyticsService
import dev.bwaim.kustomalarm.core.ApplicationScope
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
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timer

@HiltViewModel
internal class RingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    settingsService: SettingsService,
    @ApplicationContext private val appContext: Context,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val analyticsService: AnalyticsService,
    private val alarmService: AlarmService,
) : ViewModel() {
    private val args = RingArgs(savedStateHandle)
    private val alarmId = args.id

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

    init {
        viewModelScope.launch {
            timer =
                timer(period = 300) {
                    currentTime.value = getTime()
                }

            getAlarm()
        }
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

    fun turnOffAlarm() {
        timer.cancel()
        applicationScope.launch {
            stopAlarmService()
        }
    }

    private fun stopAlarmService() {
        val intent = Intent(appContext, RingingAlarmService::class.java)
        appContext.stopService(intent)
    }
}

private class RingArgs(val id: Int) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            id = checkNotNull(savedStateHandle.get<Int>(ID_RING_ALARM_ARG)),
        )
}
