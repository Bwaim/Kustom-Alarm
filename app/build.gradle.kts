plugins {
    id("kustomalarm.android.application")
    id("kustomalarm.android.application.compose")
    id("kustomalarm.android.application.jacoco")
    id("kustomalarm.android.test.compose")
//    id("kustomalarm.hilt")
    id("kustomalarm.spotless")
//    id("com.google.gms.google-services")
//    id("com.google.firebase.crashlytics")
//    id("com.google.firebase.firebase-perf")
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
//            extra.set("enableCrashlytics", false)

            applicationIdSuffix = ".debug"

//            withGroovyBuilder {
//                "FirebasePerformance" {
//                    invokeMethod("setInstrumentationEnabled", false)
//                }
//            }
        }
        val release by getting {
//            extra.set("enableCrashlytics", true)

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
//        create("benchmark") {
//            // Enable all the optimizations from release build through initWith(release).
//            initWith(release)
//            matchingFallbacks.add("release")
//            // Debug key signing is available to everyone.
//            signingConfig = signingConfigs.getByName("debug")
//            // Only use benchmark proguard rules
//            proguardFiles("benchmark-rules.pro")
//            isMinifyEnabled = true
//            applicationIdSuffix = ".benchmark"
//        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.ui)

//    implementation 'androidx.core:core-ktx:1.8.0'
//    implementation platform('org.jetbrains.kotlin:kotlin-bom:1.8.0')
//    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.3.1'
//    implementation 'androidx.activity:activity-compose:1.5.1'
//    implementation platform('androidx.compose:compose-bom:2022.10.00')
//    implementation 'androidx.compose.ui:ui'
//    implementation 'androidx.compose.ui:ui-graphics'
//    implementation 'androidx.compose.ui:ui-tooling-preview'
//    implementation 'androidx.compose.material3:material3'
//    testImplementation 'junit:junit:4.13.2'
//    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
//    androidTestImplementation platform('androidx.compose:compose-bom:2022.10.00')
//    androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
//    debugImplementation 'androidx.compose.ui:ui-tooling'
//    debugImplementation 'androidx.compose.ui:ui-test-manifest'
}
