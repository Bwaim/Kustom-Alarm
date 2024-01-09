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

@file:OptIn(ExperimentalMaterial3Api::class)

package dev.bwaim.kustomalarm.features.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissValue.StartToEnd
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.bwaim.kustomalarm.alarm.domain.Alarm
import dev.bwaim.kustomalarm.compose.DismissBackground
import dev.bwaim.kustomalarm.compose.Header
import dev.bwaim.kustomalarm.compose.KaCenterAlignedTopAppBar
import dev.bwaim.kustomalarm.compose.KaLoader
import dev.bwaim.kustomalarm.compose.PreviewsKAlarm
import dev.bwaim.kustomalarm.compose.SurfaceCard
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmThemePreview
import dev.bwaim.kustomalarm.core.android.extensions.getAppLocale
import dev.bwaim.kustomalarm.features.alarm.components.AddAlarmButton
import dev.bwaim.kustomalarm.features.alarm.components.AlarmRow
import dev.bwaim.kustomalarm.features.alarm.edit.navigation.NO_ALARM
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
    addAlarm: (Int, Boolean) -> Unit,
    viewModel: AlarmViewModel = hiltViewModel(),
) {
    val alarms by viewModel.alarms.collectAsStateWithLifecycle()

    AlarmScreen(
        alarms = alarms,
        openDrawer = openDrawer,
        addAlarm = { id, duplicate ->
            addAlarm(id, duplicate)
            if (duplicate) {
                viewModel.logDuplicateEvent()
            }
        },
        updateAlarm = viewModel::updateAlarm,
        deleteAlarm = viewModel::deleteAlarm,
        setTemplate = viewModel::setTemplate,
    )
}

@Composable
private fun AlarmScreen(
    alarms: PersistentList<Alarm>?,
    openDrawer: () -> Unit,
    addAlarm: (Int, Boolean) -> Unit,
    updateAlarm: (Alarm) -> Unit,
    deleteAlarm: (Int) -> Unit,
    setTemplate: (Alarm) -> Unit,
) {
    Scaffold(
        topBar = {
            KaCenterAlignedTopAppBar(
                onClickNavigation = openDrawer,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SurfaceCard(modifier = Modifier.padding(horizontal = 16.dp)) {
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

                alarms.isEmpty() -> NoAlarm(addAlarm = { addAlarm(it, false) })
                else ->
                    AlarmList(
                        alarms = alarms,
                        addAlarm = addAlarm,
                        updateAlarm = updateAlarm,
                        deleteAlarm = deleteAlarm,
                        setTemplate = setTemplate,
                    )
            }
        }
    }
}

@Composable
private fun ColumnScope.NoAlarm(addAlarm: (Int) -> Unit) {
    Text(
        text = stringResource(id = string.alarm_screen_no_alarm_msg),
        style = MaterialTheme.typography.headlineSmall,
    )
    Spacer(modifier = Modifier.height(60.dp))
    AddAlarmButton(addAlarm = { addAlarm(NO_ALARM) })
}

@Composable
private fun AlarmList(
    alarms: PersistentList<Alarm>,
    addAlarm: (Int, Boolean) -> Unit,
    updateAlarm: (Alarm) -> Unit,
    deleteAlarm: (Int) -> Unit,
    setTemplate: (Alarm) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentLocale = remember { context.getAppLocale() }
    val density = LocalDensity.current
    var addButtonHeight by remember {
        mutableStateOf(48.dp)
    }
    val addButtonBottomPadding = 16.dp

    ConstraintLayout(modifier = modifier) {
        val (alarmList, addButton) = createRefs()

        LazyColumn(
            modifier =
                Modifier
                    .constrainAs(alarmList) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.preferredWrapContent
                    },
        ) {
            items(
                count = alarms.size,
                key = { alarms[it].id },
            ) { index ->
                val item = alarms[index]

                val dismissState =
                    rememberSwipeToDismissState(
                        confirmValueChange = {
                            when (it) {
                                StartToEnd -> {
                                    deleteAlarm(item.id)
                                    true
                                }

                                else -> false
                            }
                        },
                        positionalThreshold = { 300.dp.value },
                    )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = { DismissBackground(dismissState = dismissState) },
                    enableDismissFromEndToStart = false,
                ) {
                    val bottomPadding =
                        if (index == alarms.lastIndex) {
                            addButtonHeight + addButtonBottomPadding + 20.dp
                        } else {
                            5.dp
                        }
                    AlarmRow(
                        alarm = item,
                        modifier =
                            Modifier
                                .clickable { addAlarm(item.id, false) }
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 16.dp)
                                .padding(bottom = bottomPadding),
                        locale = currentLocale,
                        updateAlarm = updateAlarm,
                        deleteAlarm = { deleteAlarm(item.id) },
                        setTemplate = { setTemplate(item) },
                        modifyAlarm = { addAlarm(item.id, false) },
                        duplicateAlarm = { addAlarm(item.id, true) },
                    )
                }
            }
        }

        AddAlarmButton(
            addAlarm = { addAlarm(NO_ALARM, false) },
            modifier =
                Modifier
                    .padding(bottom = addButtonBottomPadding)
                    .onGloballyPositioned {
                        with(density) {
                            addButtonHeight = it.size.height.toDp()
                        }
                    }
                    .constrainAs(addButton) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
        )
    }
}

@Composable
@PreviewsKAlarm
private fun PreviewAlarmScreenNoAlarms() {
    KustomAlarmThemePreview {
        AlarmScreen(
            alarms = persistentListOf(),
            openDrawer = {},
            addAlarm = { _, _ -> },
            updateAlarm = {},
            deleteAlarm = {},
            setTemplate = {},
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
                        uri = "uri2",
                    ),
                    Alarm(
                        id = 1,
                        name = "alarm1",
                        time = LocalTime.of(7, 35),
                        weekDays = setOf(MONDAY, TUESDAY),
                        isOnce = false,
                        uri = "uri1",
                    ),
                    Alarm(
                        id = 3,
                        name = null,
                        time = LocalTime.of(16, 45),
                        weekDays = setOf(),
                        isOnce = true,
                        uri = "uri3",
                    ),
                ),
            openDrawer = {},
            addAlarm = { _, _ -> },
            updateAlarm = {},
            deleteAlarm = {},
            setTemplate = {},
        )
    }
}
