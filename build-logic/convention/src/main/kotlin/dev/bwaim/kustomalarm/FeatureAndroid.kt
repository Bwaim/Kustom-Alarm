package dev.bwaim.kustomalarm

import com.android.build.api.dsl.CommonExtension
import kotlinOptions
import org.gradle.api.Project

fun Project.configureFeatureAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
        }
    }
}
