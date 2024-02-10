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
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.analytics.AnalyticsService
import dev.bwaim.kustomalarm.core.ApplicationScope
import dev.bwaim.kustomalarm.core.value
import dev.bwaim.kustomalarm.settings.SettingsService
import dev.bwaim.kustomalarm.settings.appstate.domain.DEFAULT_RINGING_ALARM
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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
    @ApplicationContext private val context: Context,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val settingsService: SettingsService,
    private val analyticsService: AnalyticsService,
    private val alarmService: AlarmService,
) : ViewModel() {
    private val args = RingArgs(savedStateHandle)
    private val alarmId = args.id
    private var uri = args.uri
    var title = args.title

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

    private var ringtone = uri?.let { getRingtone(it) }

    init {
        viewModelScope.launch {
            timer =
                timer(period = 300) {
                    currentTime.value = getTime()
                }

            settingsService.setRingingAlarm(alarmId)

            if (uri == null) {
                getDeferredAlarm()
            }
        }
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

    private suspend fun getDeferredAlarm() {
        val alarm = alarmService.getAlarm(alarmId).value ?: return
        uri = alarm.uri
        title = alarm.name
        ringtone = getRingtone(alarm.uri)
    }

    private fun getRingtone(uri: String) {
        RingtoneManager.getRingtone(context, uri.toUri())
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
            settingsService.setRingingAlarm(DEFAULT_RINGING_ALARM)
        }
    }
}

private class RingArgs(val id: Int, val uri: String?, val title: String?) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            id = checkNotNull(savedStateHandle.get<Int>(ID_RING_ACTIVITY_ARG)),
            uri = savedStateHandle.get<String?>(URI_RING_ACTIVITY_ARG),
            title = savedStateHandle.get<String?>(TITLE_RING_ACTIVITY_ARG),
        )
}
