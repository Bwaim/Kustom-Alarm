
import dev.bwaim.kustomalarm.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            extensions.configure<KotlinJvmProjectExtension>("kotlin") {
                explicitApi()
            }

            dependencies {
                add("implementation", libs.findLibrary("kotlin.stdlib").get())
                add("implementation", libs.findLibrary("kotlin.coroutines.core").get())
            }
        }
    }
}
