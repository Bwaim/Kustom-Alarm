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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
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
//        register("room") {
//            id = "kustomalarm.room"
//        }
        register("unitTest") {
            id = "kustomalarm.test"
            implementationClass = "UnitTestConventionPlugin"
        }
    }
}
