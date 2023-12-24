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

package dev.bwaim.kustomalarm.features.alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.compose.Header
import dev.bwaim.kustomalarm.compose.KaCenterAlignedTopAppBar
import dev.bwaim.kustomalarm.compose.KaLoader
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.SurfaceCard
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.core.android.extensions.getAppLocale
import dev.bwaim.kustomalarm.features.alarm.components.AddAlarmButton
import dev.bwaim.kustomalarm.features.alarm.components.AlarmRow
import dev.bwaim.kustomalarm.localisation.R.string
import dev.bwaim.kustomalarm.ui.resources.R.drawable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.TUESDAY
import java.time.LocalTime

@Composable
internal fun AlarmRoute(
    openDrawer: () -> Unit,
    addAlarm: () -> Unit,
    viewModel: AlarmViewModel = hiltViewModel(),
) {
    val alarms by viewModel.alarms.collectAsStateWithLifecycle()

    AlarmScreen(
        alarms = alarms,
        openDrawer = openDrawer,
        addAlarm = addAlarm,
        updateAlarm = viewModel::updateAlarm,
    )
}

@Composable
private fun AlarmScreen(
    alarms: PersistentList<Alarm>?,
    openDrawer: () -> Unit,
    addAlarm: () -> Unit,
    updateAlarm: (Alarm) -> Unit,
) {
    Scaffold(
        topBar = {
            KaCenterAlignedTopAppBar(
                onClickNavigation = openDrawer,
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SurfaceCard {
                Header(
                    title = stringResource(id = string.alarm_screen_titre),
                    imageRes = drawable.alarm_background,
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            when {
                alarms == null ->
                    KaLoader(
                        modifier =
                            Modifier
                                .size(50.dp)
                                .align(Alignment.CenterHorizontally),
                    )

                alarms.isEmpty() -> NoAlarm(addAlarm = addAlarm)
                else ->
                    AlarmList(
                        alarms = alarms,
                        addAlarm = addAlarm,
                        updateAlarm = updateAlarm,
                    )
            }
        }
    }
}

@Composable
private fun ColumnScope.NoAlarm(addAlarm: () -> Unit) {
    Text(
        text = stringResource(id = string.alarm_screen_no_alarm_msg),
        style = MaterialTheme.typography.headlineSmall,
    )
    Spacer(modifier = Modifier.height(60.dp))
    AddAlarmButton(addAlarm = addAlarm)
}

@Composable
private fun AlarmList(
    alarms: PersistentList<Alarm>,
    addAlarm: () -> Unit,
    updateAlarm: (Alarm) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentLocale = remember { context.getAppLocale() }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            count = alarms.size,
            key = { alarms[it].id },
        ) { index ->
            val item = alarms[index]
            AlarmRow(
                alarm = item,
                modifier = Modifier.padding(bottom = 5.dp),
                locale = currentLocale,
                updateAlarm = updateAlarm,
            )
        }

        item {
            AddAlarmButton(
                addAlarm = addAlarm,
                modifier = Modifier.padding(top = 30.dp),
            )
        }
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewAlarmScreenNoAlarms() {
    KustomAlarmThemePreview {
        AlarmScreen(
            alarms = persistentListOf(),
            openDrawer = {},
            addAlarm = {},
            updateAlarm = {},
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewAlarmScreenWithAlarms() {
    KustomAlarmThemePreview {
        AlarmScreen(
            alarms =
                persistentListOf(
                    Alarm(
                        id = 2,
                        name = null,
                        time = LocalTime.of(10, 0),
                        weekDays = setOf(SATURDAY),
                        isOnce = false,
                    ),
                    Alarm(
                        id = 1,
                        name = "alarm1",
                        time = LocalTime.of(7, 35),
                        weekDays = setOf(MONDAY, TUESDAY),
                        isOnce = false,
                    ),
                    Alarm(
                        id = 3,
                        name = null,
                        time = LocalTime.of(16, 45),
                        weekDays = setOf(),
                        isOnce = true,
                    ),
                ),
            openDrawer = {},
            addAlarm = {},
            updateAlarm = {},
        )
    }
}
