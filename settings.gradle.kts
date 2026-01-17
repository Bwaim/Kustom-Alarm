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

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Kustom-Alarm"

include(":app")

include(":benchmarks")

include(":common:alarm:alarm")
include(":common:alarm:alarm-impl")
include(":common:analytics:analytics")
include(":common:analytics:analytics-impl")
include(":common:compose")
include(":common:core:core")
include(":common:core:core-android")
include(":common:database")
include(":common:datastore-proto")
include(":common:initializers")
include(":common:localisation")
include(":common:navigation")
include(":common:settings:settings")
include(":common:settings:settings-impl")
include(":common:test:android")
include(":common:test:testing")
include(":common:ui-resources")

include(":features:alarm")
include(":features:settings")
