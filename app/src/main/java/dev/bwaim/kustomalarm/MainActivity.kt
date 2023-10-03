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

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.navigation.NavigationDrawerItem
import dev.bwaim.kustomalarm.settings.theme.domain.Theme
import dev.bwaim.kustomalarm.settings.theme.domain.Theme.DARK
import dev.bwaim.kustomalarm.settings.theme.domain.Theme.LIGHT
import kotlinx.collections.immutable.toImmutableList
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

            DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { isDarkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { isDarkTheme },
                )
                onDispose {}
            }

            KustomAlarmTheme(
                darkTheme = isDarkTheme,
            ) {
                KAlarmApp(
                    navigationDrawerItems = navigationDrawerItems.toImmutableList(),
                )
            }
        }
    }
}

private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

@Composable
private fun Theme?.isDarkTheme(): Boolean {
    return when (this) {
        LIGHT -> false
        DARK -> true
        else -> isSystemInDarkTheme()
    }
}
