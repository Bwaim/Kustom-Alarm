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

package dev.bwaim.kustomalarm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.bwaim.kustomalarm.compose.LocalLogScreenView
import dev.bwaim.kustomalarm.compose.configureEdgeToEdge
import dev.bwaim.kustomalarm.compose.isDarkTheme
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.navigation.NavigationDrawerItem
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity() {
    @Inject
    internal lateinit var navigationDrawerItems: Set<@JvmSuppressWildcards NavigationDrawerItem>

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val selectedTheme by mainViewModel.selectedTheme.collectAsStateWithLifecycle()
            val isDarkTheme = selectedTheme.isDarkTheme()

            configureEdgeToEdge(isDarkTheme)

            CompositionLocalProvider(LocalLogScreenView provides mainViewModel::logScreenView) {
                KustomAlarmTheme(
                    darkTheme = isDarkTheme,
                ) {
                    KAlarmApp(
                        navigationDrawerItems = navigationDrawerItems.toPersistentList(),
                    )
                }
            }
        }
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"),
            )
            ActivityCompat.startActivityForResult(this, intent, 0, null)
        }
    }
}
