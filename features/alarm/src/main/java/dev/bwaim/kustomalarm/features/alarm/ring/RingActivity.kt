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

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.bwaim.kustomalarm.compose.KaBackground
import dev.bwaim.kustomalarm.compose.LocalLogScreenView
import dev.bwaim.kustomalarm.compose.configureEdgeToEdge
import dev.bwaim.kustomalarm.compose.isDarkTheme
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.features.alarm.ring.components.RingScreen

public const val ID_RING_ACTIVITY_ARG: String = "ID_RING_ACTIVITY_ARG"

@AndroidEntryPoint
public class RingActivity : AppCompatActivity() {
    private val ringViewModel: RingViewModel by viewModels()

    private val uri: String? by lazy(NONE) {
        intent.extras?.getString(URI_RING_ACTIVITY_ARG)
    }

    private val title: String? by lazy(NONE) {
        intent.extras?.getString(TITLE_RING_ACTIVITY_ARG)
    }

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
                    KaBackground {
                        val currentTime by ringViewModel.currentTime.collectAsStateWithLifecycle()
                        val alarm by ringViewModel.alarm.collectAsStateWithLifecycle()

                        RingScreen(
                            currentTime = currentTime,
                            name = "name",
                            modifier =
                                Modifier
                                    .windowInsetsPadding(WindowInsets.safeContent),
                        )
                    }
                }
            }
        }
    }

    public companion object {
        public fun createIntent(
            context: Context,
            id: Int,
        ): Intent {
            val intent = Intent(context, RingActivity::class.java)
            intent.putExtras(
                bundleOf(
                    ID_RING_ACTIVITY_ARG to id,
                ),
            )
            return intent
        }

        internal fun createPendingIntent(context: Context, alarmId: Int): PendingIntent {
            val intent = createIntent(context, alarmId).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val stackBuilder = TaskStackBuilder.create(context).apply {
                addNextIntentWithParentStack(intent)
            }
            // requestCode should not be null for flags working correctly
            return stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
            // TODO : check when launching alarm to no have the backstack
        }
    }
}
