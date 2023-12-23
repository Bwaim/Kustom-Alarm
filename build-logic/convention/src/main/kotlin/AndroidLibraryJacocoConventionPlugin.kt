
import dev.bwaim.kustomalarm.configureJacocoLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryJacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureJacocoLibrary()
        }
    }
}
