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

import android.content.Context
import android.database.Cursor
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SoundSelectionViewModel
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _soundList: MutableStateFlow<PersistentList<Pair<String, String>>?> = MutableStateFlow(null)
        val soundList: StateFlow<PersistentList<Pair<String, String>>?> = _soundList.asStateFlow()
        private var _selectedUri: MutableStateFlow<String?> = MutableStateFlow(null)
        val selectedUri: StateFlow<String?> = _selectedUri.asStateFlow()

        private var ringtone: Ringtone? = null

        init {
            val ringtoneManager =
                RingtoneManager(ContextCompat.getContextForLanguage(context)).also {
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
                    ringtone?.play()
                }
                ringtone?.isPlaying == true -> ringtone?.stop()
                else -> ringtone?.play()
            }
        }

        private fun setRingtone(uri: String) {
            _selectedUri.value = uri
            ringtone = RingtoneManager.getRingtone(context, uri.toUri())
            ringtone?.audioAttributes = buildAlarmAudioAttribute()
        }

        private fun buildAlarmAudioAttribute(): AudioAttributes {
            return AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        }
    }

private fun Cursor.toRingtoneUri(): String {
    return "${getString(RingtoneManager.URI_COLUMN_INDEX)}/${getInt(RingtoneManager.ID_COLUMN_INDEX)}"
}
