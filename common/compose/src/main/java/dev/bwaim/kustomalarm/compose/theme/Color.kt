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

package dev.bwaim.kustomalarm.compose.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val Purple80 = Color(0xFFD0BCFF)
internal val PurpleGrey80 = Color(0xFFCCC2DC)
internal val Pink80 = Color(0xFFEFB8C8)

internal val Purple40 = Color(0xFF6650a4)
internal val PurpleGrey40 = Color(0xFF625b71)
internal val Pink40 = Color(0xFF7D5260)

internal val BlueSky = Color(0xFF98CBF0)
internal val BlueSky30 = Color(0xFF3197e1)
internal val BlueSky60 = Color(0xFF145889)

internal val BlueNight = Color(0xFF0D224A)

internal val NeutralVariant30 = Color(red = 73, green = 69, blue = 79)
internal val NeutralVariant50 = Color(red = 121, green = 116, blue = 126)
internal val NeutralVariant70 = Color(red = 174, green = 169, blue = 180)
internal val NeutralVariant80 = Color(red = 202, green = 196, blue = 208)
internal val NeutralVariant90 = Color(red = 231, green = 224, blue = 236)
internal val NeutralVariant95 = Color(red = 245, green = 238, blue = 250)

internal val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = BlueNight,
    outlineVariant = NeutralVariant30,
)

internal val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = BlueSky,
    outlineVariant = NeutralVariant80,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
