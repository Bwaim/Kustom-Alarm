
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.kapt")
            }
            extensions.configure<LibraryExtension> {
//                defaultConfig {
//                    testInstrumentationRunner =
//                        "dev.bwaim.kustomalarm.test.android.HiltTestRunner"
//                }

                extensions.configure<KaptExtension> {
                    correctErrorTypes = true
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", project(":common:navigation"))

                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

                add("implementation", libs.findLibrary("accompanist.navigation.animation").get())

                add("implementation", libs.findLibrary("kotlin.coroutines.android").get())

                add("implementation", libs.findLibrary("hilt.library").get())
                add("kapt", libs.findLibrary("hilt.compiler").get())

//                add("androidTestImplementation", project(":common:test:android"))
            }
        }
    }
}
