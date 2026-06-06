/*
 * Copyright (c) 2025-2026 Dev Bwaim team
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


import com.android.build.api.variant.TestAndroidComponentsExtension
import com.android.build.api.dsl.TestExtension
import dev.bwaim.kustomalarm.configureGradleManagedDevices
import dev.bwaim.kustomalarm.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("MagicNumber")
class BenchmarkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
            }

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 37
                configureGradleManagedDevices(this)

                defaultConfig {
                    testInstrumentationRunner = "dev.bwaim.kustomalarm.test.android.KustomAlarmTestRunner"
                }

                buildTypes {
                    // This benchmark buildType is used for benchmarking, and should function like your
                    // release build (for example, with minification on). It"s signed with a debug key
                    // for easy local/CI testing.
                    create("benchmark") {
                        isDebuggable = true
                        signingConfig = getByName("debug").signingConfig
                        matchingFallbacks += listOf("release")
                    }
                }

                targetProjectPath = ":app"
                experimentalProperties["android.experimental.self-instrumenting"] = true
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", project(":common:test:android"))
                add("implementation", libs.findLibrary("androidx-test-espresso-core").get())
                add("implementation", libs.findLibrary("androidx-test-uiautomator").get())
                add("implementation", libs.findLibrary("androidx-benchmark-macro").get())
                add("implementation", libs.findLibrary("kotlin.coroutines.core").get())
            }

            extensions.configure<TestAndroidComponentsExtension> {
                beforeVariants(selector().all()) {
                    it.enable = it.buildType == "benchmark"
                }
            }
        }
    }
}
