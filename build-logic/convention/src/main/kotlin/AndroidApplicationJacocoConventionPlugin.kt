
import dev.bwaim.kustomalarm.configureJacocoApplication
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationJacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureJacocoApplication()
        }
    }
}
