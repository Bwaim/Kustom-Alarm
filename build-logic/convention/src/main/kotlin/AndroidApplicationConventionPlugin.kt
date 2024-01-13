import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.bwaim.kustomalarm.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<BaseAppModuleExtension> {
                configureKotlinAndroid(
                    commonExtension = this,
                    androidComponentsExtension = extensions.getByType<ApplicationAndroidComponentsExtension>()
                )
                defaultConfig.targetSdk = 34
            }
        }
    }
}
