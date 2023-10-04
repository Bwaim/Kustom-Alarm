
import dev.bwaim.kustomalarm.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
            }

//            extensions.configure<LibraryExtension> {
//                defaultConfig {
//                    testInstrumentationRunner = "dev.bwaim.kustomalarm.test.android.HiltTestRunner"
//                }
//            }

            dependencies {
//                add("androidTestImplementation", project(":common:test:android"))
                add("androidTestImplementation", libs.findLibrary("kotlin.coroutines.test").get())
                add("androidTestImplementation", libs.findLibrary("junit-library").get())
                // force upgrade to 1.1.0 because its required by androidTestImplementation,
                // and without this statement AGP will silently downgrade to tracing:1.0.0
                add("implementation", libs.findLibrary("androidx-tracing").get())
            }
        }
    }
}
