plugins {
    `kotlin-dsl`
}

group = "dev.bwaim.kustomalarm.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.spotless.gradlePlugin)
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
        register("androidApplicationJacoco") {
            id = "kustomalarm.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }
//        register("androidBenchmark") {
//            id = "loteriamexicana.android.benchmark"
//            implementationClass = "BenchmarkConventionPlugin"
//        }
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
        register("spotless") {
            id = "kustomalarm.spotless"
            implementationClass = "SpotlessConventionPlugin"
        }
        register("unitTest") {
            id = "kustomalarm.test"
            implementationClass = "UnitTestConventionPlugin"
        }
    }
}
