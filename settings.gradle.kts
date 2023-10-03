pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
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

include(":common:compose")
include(":common:core:core")
include(":common:core:core-android")
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
