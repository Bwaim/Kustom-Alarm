
import dev.bwaim.kustomalarm.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.firebase-perf")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                add("implementation", libs.findLibrary("firebase.analytics.library").get())
                add("implementation", libs.findLibrary("firebase.crashlytics.library").get())
                add("implementation", libs.findLibrary("firebase.perf.library").get())
            }
        }
    }
}
