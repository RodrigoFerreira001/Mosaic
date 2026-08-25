package dev.catbit.mosaic.sample.buildconfig

import org.gradle.api.Plugin
import org.gradle.api.Project

// "local" targets the sample-server running on this same machine (`make run-sample-server`,
// always port 9090 — see sample-server/.../Application.kt). localhost resolves correctly for
// Desktop and Web (same-machine browser); an Android emulator needs "http://10.0.2.2:9090"
// instead, and a physical Android device needs this machine's LAN IP — override by passing
// -Pmosaic.environment=local and editing the map below, or by adding a new named entry.
private val ENVIRONMENT_BASE_URLS = mapOf(
    "local" to "http://localhost:9090",
    "remote" to "https://mosaicsampleserver.web.app",
)

class BuildConfigPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("buildConfig", BuildConfigExtension::class.java)

        val environment = (project.findProperty("mosaic.environment") as? String) ?: "local"
        val baseUrl = ENVIRONMENT_BASE_URLS[environment]
            ?: throw IllegalArgumentException(
                "Invalid mosaic.environment '$environment' — expected one of: ${ENVIRONMENT_BASE_URLS.keys.joinToString()}"
            )

        project.tasks.register("generateBuildConfig") {
            val outputDir = project.layout.buildDirectory.dir("generated/buildconfig")
            outputs.dir(outputDir)
            inputs.property("mosaicEnvironment", environment)
            inputs.property("packageName", extension.packageName)

            doLast {
                val packageName = extension.packageName.get()
                val packagePath = packageName.replace('.', '/')
                val dir = outputDir.get().asFile.resolve(packagePath)
                dir.mkdirs()
                dir.resolve("BuildConfig.kt").writeText(buildString {
                    appendLine("package $packageName")
                    appendLine()
                    appendLine("object BuildConfig {")
                    appendLine("    const val ENVIRONMENT: String = \"$environment\"")
                    appendLine("    const val IS_LOCAL: Boolean = ${environment == "local"}")
                    appendLine("    const val BASE_URL: String = \"$baseUrl\"")
                    appendLine("}")
                })
            }
        }
    }
}
