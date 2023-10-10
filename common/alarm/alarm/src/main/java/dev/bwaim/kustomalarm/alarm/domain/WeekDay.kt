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

package dev.bwaim.kustomalarm.alarm.domain

public enum class WeekDay(public val value: Int) {
    MONDAY(0b1000000),
    TUESDAY(0b0100000),
    WEDNESDAY(0b0010000),
    THURSDAY(0b0001000),
    FRIDAY(0b0000100),
    SATURDAY(0b0000010),
    SUNDAY(0b0000001),
}
