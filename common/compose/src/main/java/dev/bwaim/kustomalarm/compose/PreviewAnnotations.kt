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

package dev.bwaim.kustomalarm.compose

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "1_light theme",
    group = "themes",
    showBackground = true,
    backgroundColor = 0xffeeebe8,
    device = "spec:shape=Normal,width=360,height=720,unit=dp,dpi=480",
)
public annotation class LightPreview

@Preview(
    name = "2_dark theme",
    group = "themes",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
    backgroundColor = 0xff1d1516,
    device = "spec:shape=Normal,width=360,height=720,unit=dp,dpi=480",
)
public annotation class DarkPreview

@Preview(
    name = "3_tablet",
    group = "formFactor",
    showBackground = true,
    backgroundColor = 0xffeeebe8,
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480",
)
public annotation class TabletPreview

@Preview(
    name = "4_small_phone",
    group = "formFactor",
    showBackground = true,
    backgroundColor = 0xffeeebe8,
    device = "spec:shape=Normal,width=360,height=720,unit=dp,dpi=240",
)
public annotation class SmallPhonePreview

@LightPreview
@DarkPreview
public annotation class DarkLightPreviews

@TabletPreview
@SmallPhonePreview
public annotation class DeviceLightPreviews

@DarkLightPreviews
@DeviceLightPreviews
public annotation class KAlarmPreviews
