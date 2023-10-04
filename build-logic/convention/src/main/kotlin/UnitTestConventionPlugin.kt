
import dev.bwaim.kustomalarm.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class UnitTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("testImplementation", project(":common:test:testing"))
                add("testImplementation", libs.findLibrary("junit-library").get())
                add("testImplementation", libs.findLibrary("kotlin.coroutines.test").get())
                add("testImplementation", libs.findLibrary("cash.turbine").get())
            }
        }
    }
}
