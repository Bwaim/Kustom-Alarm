/*
 * Copyright (c) 2025 Dev Bwaim team
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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "dev.bwaim.kustomalarm.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.room.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.protobuf.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "kustomalarm.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "kustomalarm.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationFirebase") {
            id = "kustomalarm.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidApplicationJacoco") {
            id = "kustomalarm.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }
        register("androidBenchmark") {
            id = "kustomalarm.android.benchmark"
            implementationClass = "BenchmarkConventionPlugin"
        }
        register("androidFeature") {
            id = "kustomalarm.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "kustomalarm.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "kustomalarm.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryJacoco") {
            id = "kustomalarm.android.library.jacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }
        register("androidTestCompose") {
            id = "kustomalarm.android.test.compose"
            implementationClass = "AndroidTestComposeConventionPlugin"
        }
        register("androidTest") {
            id = "kustomalarm.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("hilt") {
            id = "kustomalarm.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("kotlin") {
            id = "kustomalarm.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("kotlinJacoco") {
            id = "kustomalarm.kotlin.library.jacoco"
            implementationClass = "KotlinLibraryJacocoConventionPlugin"
        }
        register("protobuf") {
            id = "kustomalarm.protobuf"
            implementationClass = "ProtobufConventionPlugin"
        }
        register("room") {
            id = "kustomalarm.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("unitTest") {
            id = "kustomalarm.test"
            implementationClass = "UnitTestConventionPlugin"
        }
    }
}
