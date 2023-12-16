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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.alarm.AlarmService
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.core.Result.Error
import dev.bwaim.kustomalarm.core.Result.Success
import dev.bwaim.kustomalarm.core.SaveEvents
import dev.bwaim.kustomalarm.core.android.extensions.formatDuration
import dev.bwaim.kustomalarm.core.extentions.durationTo
import dev.bwaim.kustomalarm.localisation.R.string
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
internal class EditViewModel
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val alarmService: AlarmService,
    ) : ViewModel() {
        private val _saveEventsFlow: MutableSharedFlow<SaveEvents> = MutableSharedFlow(extraBufferCapacity = 1)
        val saveEventsFlow: SharedFlow<SaveEvents> = _saveEventsFlow.asSharedFlow()

        fun saveAlarm(alarm: Alarm) {
            viewModelScope.launch {
                val event =
                    when (alarmService.saveAlarm(alarm = alarm)) {
                        is Success -> {
                            val now = LocalTime.now()
                            val adjustedNow = LocalTime.of(now.hour, now.minute)
                            val duration = adjustedNow.durationTo(alarm.time)
                            val timeBeforeAlarm = context.formatDuration(duration)

                            SaveEvents.Success(context.getString(string.edit_alarm_screen_saving_success, timeBeforeAlarm))
                        }
                        is Error -> SaveEvents.Failure(context.getString(string.edit_alarm_screen_saving_error))
                    }
                _saveEventsFlow.tryEmit(event)
            }
        }
    }
