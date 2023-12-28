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

package dev.bwaim.kustomalarm.features.alarm.edit

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.analytics.AnalyticsService
import dev.bwaim.kustomalarm.analytics.model.AlarmAddEvent
import dev.bwaim.kustomalarm.analytics.model.AlarmDeleteEvent
import dev.bwaim.kustomalarm.analytics.model.AlarmModifyEvent
import dev.bwaim.kustomalarm.core.Result.Error
import dev.bwaim.kustomalarm.core.Result.Success
import dev.bwaim.kustomalarm.core.SaveEvents
import dev.bwaim.kustomalarm.core.android.extensions.formatDuration
import dev.bwaim.kustomalarm.core.extentions.durationTo
import dev.bwaim.kustomalarm.features.alarm.edit.navigation.EditAlarmArgs
import dev.bwaim.kustomalarm.features.alarm.edit.navigation.NO_ALARM
import dev.bwaim.kustomalarm.localisation.R.string
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
internal class EditViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        @ApplicationContext private val context: Context,
        private val alarmService: AlarmService,
        private val analyticsService: AnalyticsService,
    ) : ViewModel() {
        private val args = EditAlarmArgs(savedStateHandle)
        private val alarmId = args.alarmId
        private val duplicate = args.duplicate

        private val _saveEventsFlow: MutableSharedFlow<SaveEvents> =
            MutableSharedFlow(extraBufferCapacity = 1)
        val saveEventsFlow: SharedFlow<SaveEvents> = _saveEventsFlow.asSharedFlow()

        private val _alarm: MutableStateFlow<Alarm?> = MutableStateFlow(null)
        val alarm: StateFlow<Alarm?> = _alarm.asStateFlow()
        private var initialAlarm: Alarm? = null

        private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
        val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

        init {
            viewModelScope.launch {
                _alarm.update {
                    if (alarmId == NO_ALARM) {
                        alarmService.getDefaultAlarm()
                    } else {
                        when (val alarm = alarmService.getAlarm(alarmId = alarmId)) {
                            is Success ->
                                if (duplicate) {
                                    alarm.value?.copy(id = 0)
                                } else {
                                    alarm.value
                                }
                            is Error -> {
                                _errorMessage.update {
                                    context.getString(string.edit_alarm_screen_getting_error)
                                }
                                it
                            }
                        }
                    }
                }
                initialAlarm = _alarm.value
            }
        }

        fun saveAlarm() {
            alarm.value?.let { alarm ->
                viewModelScope.launch {
                    val event =
                        when (alarmService.saveAlarm(alarm = alarm)) {
                            is Success -> {
                                val now = LocalTime.now()
                                val adjustedNow = LocalTime.of(now.hour, now.minute)
                                val duration = adjustedNow.durationTo(alarm.time)
                                val timeBeforeAlarm = context.formatDuration(duration)

                                analyticsService.logEvent(
                                    if (alarm.id == 0) {
                                        alarm.toAlarmAddEvent()
                                    } else {
                                        alarm.toAlarmModifyEvent()
                                    },
                                )

                                SaveEvents.Success(
                                    context.getString(
                                        string.edit_alarm_screen_saving_success,
                                        timeBeforeAlarm,
                                    ),
                                )
                            }

                            is Error -> SaveEvents.Failure(context.getString(string.edit_alarm_screen_saving_error))
                        }
                    _saveEventsFlow.tryEmit(event)
                }
            }
        }

        fun updateAlarmName(name: String) {
            _alarm.update {
                it?.copy(name = name)
            }
        }

        fun updateAlarmTime(time: LocalTime) {
            _alarm.update {
                it?.copy(time = time)
            }
        }

        fun updateAlarmDays(days: Set<DayOfWeek>) {
            _alarm.update {
                it?.copy(weekDays = days)
            }
        }

        fun hasModification(): Boolean {
            return initialAlarm != _alarm.value
        }

        fun deleteAlarm() {
            viewModelScope.launch {
                if (alarmId != NO_ALARM) {
                    alarmService.deleteAlarm(alarmId = alarmId)
                    analyticsService.logEvent(AlarmDeleteEvent)
                }
            }
        }
    }

private fun Alarm.toAlarmAddEvent(): AlarmAddEvent =
    AlarmAddEvent(
        time = time,
        nbDays = weekDays.size.toLong(),
    )

private fun Alarm.toAlarmModifyEvent(): AlarmModifyEvent =
    AlarmModifyEvent(
        time = time,
        nbDays = weekDays.size.toLong(),
    )
