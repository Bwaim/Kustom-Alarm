package dev.bwaim.kustomalarm

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.coverage.JacocoReportTask
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

private val coverageExclusions = listOf(
    // Android
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*"
)

interface JacocoKustomExtension {
    var hasTests: Boolean
}

open class JacocoKustomExtensionImpl : JacocoKustomExtension {
    override var hasTests: Boolean = true
}

fun Project.configureJacocoApplication() {
    val jacocoKustomExtension = configureJacocoKustomExtension()

    extensions.configure<ApplicationExtension> {
        buildTypes {
            debug {
                enableUnitTestCoverage = true
            }
        }
        testOptions {
            unitTests.all {
                it.configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
        }
    }

    configureJacocoAfterEvaluate(jacocoKustomExtension)
}

fun Project.configureJacocoLibrary() {
    val jacocoKustomExtension = configureJacocoKustomExtension()

    extensions.configure<LibraryExtension> {
        buildTypes {
            debug {
                enableUnitTestCoverage = true
            }
        }
        testOptions {
            unitTests.all {
                it.configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
        }
    }

    configureJacocoAfterEvaluate(jacocoKustomExtension)
}

fun Project.configureJacocoKustomExtension(): JacocoKustomExtension {
    return extensions.create(
        JacocoKustomExtension::class.java,
        "jacocoKustomConfig",
        JacocoKustomExtensionImpl::class.java
    )
}

fun Project.configureJacocoAfterEvaluate(jacocoKustomExtension: JacocoKustomExtension) {
    afterEvaluate {
        if (!jacocoKustomExtension.hasTests) {
            tasks.withType<JacocoReportTask> {
                enabled = false
            }
        }
    }
}

fun Project.configureJacoco() {
    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }

    tasks.withType<Test> {
        finalizedBy(tasks.withType<JacocoReport>()) // report is always generated after tests run

        configure<JacocoTaskExtension> {
            isEnabled = true
        }
    }

    tasks.withType<JacocoReport> {
        dependsOn(tasks.withType<Test>())

        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}
