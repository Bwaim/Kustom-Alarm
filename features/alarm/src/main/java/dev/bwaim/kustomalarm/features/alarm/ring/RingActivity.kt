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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.bwaim.kustomalarm.compose.LocalLogScreenView
import dev.bwaim.kustomalarm.compose.configureEdgeToEdge
import dev.bwaim.kustomalarm.compose.isDarkTheme
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme

@AndroidEntryPoint
internal class RingActivity : AppCompatActivity() {
    private val ringViewModel: RingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val selectedTheme by ringViewModel.selectedTheme.collectAsStateWithLifecycle()
            val isDarkTheme = selectedTheme.isDarkTheme()

            configureEdgeToEdge(isDarkTheme)

            CompositionLocalProvider(LocalLogScreenView provides ringViewModel::logScreenView) {
                KustomAlarmTheme(
                    darkTheme = isDarkTheme,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Ring !!!!!!!!!!!!!!")
                    }
                }
            }
        }
    }
}
