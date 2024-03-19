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

package dev.bwaim.kustomalarm.features.alarm

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.alarm.domain.AlarmTemplate
import dev.bwaim.kustomalarm.analytics.AnalyticsService
import dev.bwaim.kustomalarm.analytics.model.AlarmDeleteEvent
import dev.bwaim.kustomalarm.analytics.model.AlarmDisableEvent
import dev.bwaim.kustomalarm.analytics.model.AlarmDuplicateEvent
import dev.bwaim.kustomalarm.analytics.model.AlarmEnableEvent
import dev.bwaim.kustomalarm.analytics.model.AlarmPreviewEvent
import dev.bwaim.kustomalarm.analytics.model.AlarmSetTemplateEvent
import dev.bwaim.kustomalarm.features.alarm.ring.RingActivity
import dev.bwaim.kustomalarm.settings.SettingsService
import dev.bwaim.kustomalarm.settings.appstate.domain.NOT_SAVED_ALARM_ID
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AlarmViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    settingsService: SettingsService,
    private val alarmService: AlarmService,
    private val analyticsService: AnalyticsService,
) : ViewModel() {
    val alarms: StateFlow<PersistentList<Alarm>?> =
        alarmService
            .observeAlarms()
            .map { it.toPersistentList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private var checkingAlarmJob: Job? = null

    init {
        checkingAlarmJob = combine(
            settingsService.observeRingingAlarm(),
            alarmService.observeSnoozedAlarm(),
        ) { ringingAlarmId, snoozedAlarm ->
            if (ringingAlarmId != NOT_SAVED_ALARM_ID) {
                ringingAlarmId
            } else {
                snoozedAlarm?.id ?: NOT_SAVED_ALARM_ID
            }
        }
            .onEach {
                if (it != NOT_SAVED_ALARM_ID) {
                    val intent = RingActivity.createIntent(
                        context = appContext,
                        alarmId = it,
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    )
                    appContext.startActivity(intent)
                }
                checkingAlarmJob?.cancel()
            }
            .launchIn(viewModelScope)
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmService.saveAlarm(alarm = alarm)
            analyticsService.logEvent(
                if (alarm.isActivated) {
                    AlarmEnableEvent
                } else {
                    AlarmDisableEvent
                },
            )
        }
    }

    fun deleteAlarm(alarmId: Int) {
        viewModelScope.launch {
            alarmService.deleteAlarm(alarmId)
            analyticsService.logEvent(AlarmDeleteEvent)
        }
    }

    fun logDuplicateEvent() {
        viewModelScope.launch {
            analyticsService.logEvent(AlarmDuplicateEvent)
        }
    }

    fun setTemplate(alarm: Alarm) {
        viewModelScope.launch {
            alarmService.saveTemplate(alarm.toTemplate())
            analyticsService.logEvent(AlarmSetTemplateEvent)
        }
    }

    fun trackPreviewEvent() {
        viewModelScope.launch {
            analyticsService.logEvent(AlarmPreviewEvent)
        }
    }
}

internal fun Alarm.toTemplate(): AlarmTemplate =
    AlarmTemplate(
        name = name,
        time = time,
        weekDays = weekDays,
        uri = uri,
        postponeDuration = postponeDuration,
    )
