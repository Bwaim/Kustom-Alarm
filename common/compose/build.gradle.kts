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
    id("kustomalarm.android.library")
    id("kustomalarm.android.library.compose")
    id("kustomalarm.android.library.jacoco")
}

android {
    namespace = "dev.bwaim.kustomalarm.compose"
}

jacocoKustomConfig {
    hasTests = false
}

dependencies {
    implementation(projects.common.localisation)
    implementation(projects.common.settings.settings)
    implementation(projects.common.uiResources)

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)

    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui.tooling.preview)

    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.accompanist.permissions)

    implementation(libs.snapper)

//    implementation("androidx.compose.material:material-icons-extended:1.6.0-beta03")
}
