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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bwaim.kustomalarm.analytics.AnalyticsService
import dev.bwaim.kustomalarm.settings.SettingsService
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
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
internal class RingViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        settingsService: SettingsService,
        private val analyticsService: AnalyticsService,
    ) : ViewModel() {
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
            }
        }

        override fun onCleared() {
            timer.cancel()
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
    }
