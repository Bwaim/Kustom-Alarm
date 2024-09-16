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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import dev.bwaim.kustomalarm.core.android.BuildWrapper
import dev.bwaim.kustomalarm.settings.appstate.domain.NOT_SAVED_ALARM_ID

private const val EXTRA_ALARM_ID: String = "EXTRA_ALARM_ID"

@AndroidEntryPoint
public class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent?,
    ) {
        val alarmId = intent?.getIntExtra(EXTRA_ALARM_ID, NOT_SAVED_ALARM_ID) ?: return
        startAlarmService(context, alarmId)
    }

    private fun startAlarmService(
        context: Context,
        alarmId: Int,
    ) {
        val intent =
            RingingAlarmService.createIntent(
                context = context,
                alarmId = alarmId,
                alarmUri = null,
            )

        if (BuildWrapper.isAtLeastO) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    public companion object {
        public fun createIntent(
            context: Context,
            alarmId: Int,
        ): Intent =
            Intent(context, AlarmBroadcastReceiver::class.java).apply {
                putExtra(EXTRA_ALARM_ID, alarmId)
            }
    }
}
