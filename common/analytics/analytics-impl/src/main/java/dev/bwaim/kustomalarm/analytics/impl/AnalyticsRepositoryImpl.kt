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

package dev.bwaim.kustomalarm.analytics.impl

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import dev.bwaim.kustomalarm.analytics.AnalyticsRepository
import dev.bwaim.kustomalarm.analytics.model.KaEvent
import javax.inject.Inject

internal class AnalyticsRepositoryImpl @Inject constructor() : AnalyticsRepository {
    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override suspend fun logScreenView(
        screenName: String,
        screenClass: String,
    ) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }

    override suspend fun logEvent(event: KaEvent) {
        firebaseAnalytics.logEvent(event.name) {
            event.params.forEach { (name, value) ->
                when (value) {
                    is Bundle -> param(name, value)
                    is Double -> param(name, value)
                    is Long -> param(name, value)
                    is String -> param(name, value)
                    else -> throw IllegalArgumentException("Wrong type for event parameter : ${value::class.java}")
                }
            }
        }
    }
}
