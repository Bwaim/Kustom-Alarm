import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

fun KotlinJvmCompilerOptions.context() {
    freeCompilerArgs.add("-Xcontext-receivers")
}