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

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.bwaim.kustomalarm.compose.KaCloseTopAppBar
import dev.bwaim.kustomalarm.compose.KaLoader
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.core.android.extensions.toast
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
internal fun SoundSelectionRoute(
    close: () -> Unit,
    viewModel: SoundSelectionViewModel = hiltViewModel(),
) {
    val soundList by viewModel.soundList.collectAsStateWithLifecycle()
    val selectedUri by viewModel.selectedUri.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.noVolumeEvent
            .onEach { context.toast(it, duration = Toast.LENGTH_SHORT) }
            .launchIn(this)
    }

    SoundSelectionScreen(
        soundList = soundList,
        selectedUri = selectedUri ?: "",
        close = close,
        play = viewModel::playRingtone,
    )
}

@Composable
private fun SoundSelectionScreen(
    soundList: PersistentList<Pair<String, String>>?,
    selectedUri: String,
    close: () -> Unit,
    play: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            KaCloseTopAppBar(
                onClickNavigation = close,
                title = {
                    Text(text = "Sound selection")
                },
            )
        },
    ) { padding ->
        when (soundList) {
            null ->
                KaLoader(
                    modifier =
                        Modifier
                            .padding(padding)
                            .fillMaxSize(),
                )

            else ->
                SoundList(
                    sounds = soundList,
                    selectedUri = selectedUri,
                    play = play,
                    modifier = Modifier.padding(padding),
                )
        }
    }
}

@Composable
private fun SoundList(
    sounds: PersistentList<Pair<String, String>>,
    selectedUri: String,
    play: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .padding(16.dp)
                .fillMaxWidth(),
    ) {
        items(
            count = sounds.size,
        ) { index ->
            val item = sounds[index]
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { play(item.second) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = item.second == selectedUri, onClick = { })
                Text(text = item.first)
            }
        }
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewSoundSelectionScreen() {
    KustomAlarmThemePreview {
        SoundSelectionScreen(
            soundList =
                persistentListOf(
                    "sound1" to "uri1",
                    "sound2" to "uri2",
                ),
            selectedUri = "uri2",
            close = {},
            play = {},
        )
    }
}
