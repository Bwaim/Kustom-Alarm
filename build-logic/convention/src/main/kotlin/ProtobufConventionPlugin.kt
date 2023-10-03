
import com.google.protobuf.gradle.ProtobufExtension
import com.google.protobuf.gradle.id
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class ProtobufConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.protobuf")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", libs.findLibrary("protobuf-kotlin-lite").get())
            }

            extensions.findByType<ProtobufExtension>()!!.apply {
                protoc {
                    artifact = libs.findLibrary("protobuf-protoc").get().toArtifactSpec()
                }

                generateProtoTasks {
                    all().configureEach {
                        builtins {
                            id("java") {
                                option("lite")
                            }
                            id("kotlin") {
                                option("lite")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Provider<MinimalExternalModuleDependency>.toArtifactSpec(): String {
    val externalModuleDependency = get()
    val module = externalModuleDependency.module
    val group = module.group
    val name = module.name
    val version = externalModuleDependency.versionConstraint.displayName
    return "${group}:${name}:${version}"
}
