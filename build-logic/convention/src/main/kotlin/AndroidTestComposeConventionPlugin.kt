import dev.bwaim.kustomalarm.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
            }

            dependencies {
                add("androidTestImplementation", project(":common:test:android"))
                add("androidTestImplementation", libs.findLibrary("compose.ui.test").get())
                add("debugImplementation", libs.findLibrary("compose.ui.test.manifest").get())
            }
        }
    }
}
