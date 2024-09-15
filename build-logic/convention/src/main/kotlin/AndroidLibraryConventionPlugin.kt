import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import dev.bwaim.kustomalarm.configureKotlinAndroid
import dev.bwaim.kustomalarm.disableUnnecessaryAndroidTests
import dev.bwaim.kustomalarm.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(
                    commonExtension = this,
                    androidComponentsExtension = extensions.getByType<LibraryAndroidComponentsExtension>()
                )
                defaultConfig.targetSdk = 35
            }

            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }

            dependencies {
                add("implementation", project(":common:core:core"))

                add("implementation", libs.findLibrary("kotlin.coroutines.core").get())

                configurations.configureEach {
                    resolutionStrategy {
                        force(libs.findLibrary("junit-library").get())
                    }
                }
            }
        }
    }

}
