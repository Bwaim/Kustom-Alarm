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
plugins {
    id("kustomalarm.android.application")
    id("kustomalarm.android.application.compose")
    id("kustomalarm.android.application.firebase")
    id("kustomalarm.android.application.jacoco")
    id("kustomalarm.android.test.compose")
    id("kustomalarm.hilt")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "dev.bwaim.kustomalarm"

    defaultConfig {
        applicationId = "dev.bwaim.kustomalarm"
        versionCode = 1
        versionName = "1.0.0"

//        testInstrumentationRunner = "dev.bwaim.kustomalarm.test.android.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resourceConfigurations.addAll(listOf("en", "es", "fr"))
    }

    buildTypes {
        val debug by getting {
            extra.set("enableCrashlytics", false)

            applicationIdSuffix = ".debug"

            configure<com.google.firebase.perf.plugin.FirebasePerfExtension> {
                setInstrumentationEnabled(false)
            }
        }
        val release by getting {
            extra.set("enableCrashlytics", true)

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            // Enable all the optimizations from release build through initWith(release).
            initWith(release)
            matchingFallbacks.add("release")
            // Debug key signing is available to everyone.
            signingConfig = signingConfigs.getByName("debug")
            // Only use benchmark proguard rules
            proguardFiles("benchmark-rules.pro")
            isMinifyEnabled = true
            applicationIdSuffix = ".benchmark"
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.common.alarm.alarmImpl)
    implementation(projects.common.compose)
    implementation(projects.common.core.coreAndroid)
    implementation(projects.common.database)
    implementation(projects.common.navigation)
    implementation(projects.common.settings.settings)
    implementation(projects.common.settings.settingsImpl)
    implementation(projects.common.uiResources)

    implementation(projects.features.alarm)
    implementation(projects.features.settings)

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.profile.installer)

    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.ui)
}
