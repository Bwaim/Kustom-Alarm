
import dev.bwaim.kustomalarm.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("com.google.devtools.ksp")
            }

            dependencies {
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("hilt.library").get())
            }
        }
    }
}
