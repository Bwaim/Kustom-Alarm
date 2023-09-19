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
include(":common:initializers")
include(":common:navigation")
include(":common:ui-resources")

include(":features:alarm")
