package dev.bwaim.kustomalarm

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.google.protobuf.gradle.GenerateProtoTask
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure base Kotlin with Android options
 */
fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>? = null,
) {
    commonExtension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 24
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            isCoreLibraryDesugaringEnabled = true
        }

        lint {
            // TODO remove when this bug is fixed : https://issuetracker.google.com/issues/196406778
            disable.add("Instantiatable")
        }
    }

    // TODO remove this when the bug is fixed
    androidComponentsExtension?.apply {
        onVariants { variant ->
            afterEvaluate {
                // This is a workaround for https://github.com/google/ksp/issues/1590 (follow also https://issuetracker.google.com/301245705) which depends on internal
                // implementations of the android gradle plugin and the ksp gradle plugin which might change in the future
                // in an unpredictable way.
                val variantNameCapitalized = variant.name.replaceFirstChar { it.uppercase() }
                val protoTaskName = "generate${variantNameCapitalized}Proto"

                tasks.filter { it.name == protoTaskName }.forEach {
                    val protoTask = it as GenerateProtoTask

                    tasks.getByName("ksp${variantNameCapitalized}Kotlin") {
                        dependsOn(protoTask)
                        (this as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>).setSource(
                            protoTask.outputBaseDir
                        )
                    }
                }
            }
        }
    }

    configureKotlin()

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlin() {
    // Use withType to workaround https://youtrack.jetbrains.com/issue/KT-55947
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            // Set JVM target to 17
            jvmTarget.set(JVM_17)

            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()

            freeCompilerArgs.addAll(
                listOf(
                    "-Xexplicit-api=strict",
                    "-opt-in=kotlin.RequiresOptIn",
                    // Enable experimental coroutines APIs, including Flow
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                )
            )
        }
    }
}
