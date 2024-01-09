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

package dev.bwaim.kustomalarm.features.alarm.sound

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.bwaim.kustomalarm.core.android.extensions.toRingtoneUri
import dev.bwaim.kustomalarm.features.alarm.sound.navigation.SoundSelectionArgs
import dev.bwaim.kustomalarm.localisation.R.string
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SoundSelectionViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        @ApplicationContext private val appContext: Context,
    ) : ViewModel() {
        private val args = SoundSelectionArgs(savedStateHandle)
        private val uri = args.uri

        private val _soundList: MutableStateFlow<PersistentList<Pair<String, String>>?> =
            MutableStateFlow(null)
        val soundList: StateFlow<PersistentList<Pair<String, String>>?> = _soundList.asStateFlow()
        private var _selectedUri: MutableStateFlow<String> = MutableStateFlow(uri)
        val selectedUri: StateFlow<String> = _selectedUri.asStateFlow()

        private var ringtone: Ringtone? = null

        @SuppressLint("StaticFieldLeak")
        private val context = ContextCompat.getContextForLanguage(appContext)
        private var _noVolumeEvent: MutableSharedFlow<String> = MutableSharedFlow(extraBufferCapacity = 1)
        val noVolumeEvent: SharedFlow<String> = _noVolumeEvent.asSharedFlow()

        init {
            setRingtone(uri)

            val ringtoneManager =
                RingtoneManager(ContextCompat.getContextForLanguage(appContext)).also {
                    it.setType(RingtoneManager.TYPE_ALARM or RingtoneManager.TYPE_RINGTONE)
                }
            val cursor = ringtoneManager.cursor
            viewModelScope.launch {
                val tmpList = mutableListOf<Pair<String, String>>()
                while (cursor.moveToNext()) {
                    val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                    val uri = cursor.toRingtoneUri()
                    tmpList.add(title to uri)
                }
                _soundList.value = tmpList.toPersistentList()
            }.invokeOnCompletion {
                cursor?.close()
            }
        }

        override fun onCleared() {
            ringtone?.stop()
        }

        fun playRingtone(uri: String) {
            when {
                uri != _selectedUri.value -> {
                    ringtone?.stop()
                    setRingtone(uri)
                    play()
                }

                ringtone?.isPlaying == true -> ringtone?.stop()
                else -> play()
            }
        }

        private fun setRingtone(uri: String) {
            _selectedUri.value = uri
            ringtone = RingtoneManager.getRingtone(appContext, uri.toUri())
            ringtone?.audioAttributes = buildAlarmAudioAttribute()
        }

        private fun buildAlarmAudioAttribute(): AudioAttributes {
            return AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        }

        private fun play() {
            ringtone?.play().also {
                if (ringtone?.isPlaying != true) {
                    _noVolumeEvent.tryEmit(context.getString(string.sound_selection_screen_no_volume_message))
                }
            }
        }
    }
