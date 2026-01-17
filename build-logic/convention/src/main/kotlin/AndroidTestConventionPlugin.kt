/*
 * Copyright (c) 2026 Dev Bwaim team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import com.android.build.api.dsl.LibraryExtension
import dev.bwaim.kustomalarm.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "dev.bwaim.kustomalarm.test.android.KustomAlarmTestRunner"
                }
            }

            dependencies {
                add("androidTestImplementation", project(":common:test:android"))
                add("androidTestImplementation", libs.findLibrary("kotlin.coroutines.test").get())
                add("androidTestImplementation", libs.findLibrary("junit-library").get())
                add("androidTestImplementation", libs.findLibrary("androidx-test-core").get())
                add("androidTestImplementation", libs.findLibrary("cash.turbine").get())
                // force upgrade to 1.1.0 because its required by androidTestImplementation,
                // and without this statement AGP will silently downgrade to tracing:1.0.0
                add("implementation", libs.findLibrary("androidx-tracing").get())
            }
        }
    }
}
