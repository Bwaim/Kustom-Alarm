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

package dev.bwaim.kustomalarm.analytics.model

import java.time.LocalTime

public sealed interface KaEvent {
    public val name: String
    public val params: Map<String, Any>
}

public data class AlarmAddEvent(
    val time: LocalTime,
    val nbDays: Long,
) : KaEvent {
    override val name: String = "ka_alarm_add"
    override val params: Map<String, Any> =
        mapOf(
            "ka_time" to time.toString(),
            "ka_nb_days" to nbDays,
        )
}

public data object AlarmEnableEvent : KaEvent {
    override val name: String = "ka_alarm_enable"
    override val params: Map<String, Any> = emptyMap()
}

public data object AlarmDisableEvent : KaEvent {
    override val name: String = "ka_alarm_disable"
    override val params: Map<String, Any> = emptyMap()
}

public data object AlarmDeleteEvent : KaEvent {
    override val name: String = "ka_alarm_delete"
    override val params: Map<String, Any> = emptyMap()
}
