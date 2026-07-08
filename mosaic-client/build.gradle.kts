@file:OptIn(ExperimentalWasmDsl::class)
@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.room3)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.stability.analyzer)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "dev.catbit",
        artifactId = "mosaic-client",
        version = libs.versions.mosaic.get()
    )

    pom {
        name = "Mosaic Client"
        description = "Compose Multiplatform rendering and event execution engine for the Mosaic Server-Driven UI framework."
        url = "https://github.com/RodrigoFerreira001/Mosaic"

        licenses {
            license {
                name = "Apache License 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
            }
        }

        developers {
            developer {
                id = "RodrigoFerreira001"
                name = "Rodrigo Ferreira"
                url = "https://github.com/RodrigoFerreira001"
            }
        }

        scm {
            url = "https://github.com/RodrigoFerreira001/Mosaic"
            connection = "scm:git:git://github.com/RodrigoFerreira001/Mosaic.git"
            developerConnection = "scm:git:ssh://git@github.com/RodrigoFerreira001/Mosaic.git"
        }
    }
}

kotlin {

    android {
        namespace = "dev.catbit.mosaic.client"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }

        androidResources {
            enable = true
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    wasmJs {
        browser()
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            // Mosaic core
            implementation(projects.mosaicCore)

            // Room 3
            implementation(libs.room3.runtime)

            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.components.ui.tooling.preview)
            implementation(libs.compose.ui.tooling.preview)

            // Navigation
            implementation(libs.compose.navigation3.ui)
            implementation(libs.compose.navigation3.adaptiveNavigation3)
            implementation(libs.compose.navigation3.viewmodelNavigation3)
            implementation(libs.compose.viewmodel)
            implementation(libs.compose.material3.adaptative)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Kotlin serialization
            implementation(libs.kotlinx.serialization.json)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network)

            // Shimmer
            implementation(libs.shimmer)

            // Datetime
            implementation(libs.kotlinx.datetime)

            // Kotlinx Collections Immutable
            implementation(libs.kotlinx.collections.immutable)

            // FileKit
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogs.compose)

            // CMP Image Compressor
            implementation(libs.cmp.imgcompress)
        }

        androidMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.okhttp)
            // Exif orientation handling for camera captures
            implementation(libs.androidx.exifinterface)
            // SQLite driver
            implementation(libs.androidx.sqlite.bundled)
        }

        iosMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.darwin)
            // SQLite driver
            implementation(libs.androidx.sqlite.bundled)
        }

        jvmMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.okhttp)
            // SQLite driver
            implementation(libs.androidx.sqlite.bundled)
            // Webcam Capture
            implementation(libs.webcam.capture)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        wasmJsMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.js)
            // SQLite driver (OPFS-based Web Worker)
            implementation(libs.androidx.sqlite.web)
            implementation(npm("sqlite-wasm-worker", layout.projectDirectory.dir("sqliteWasmWorker").asFile))
            // OPFS file handler (dedicated Web Worker)
            implementation(npm("opfs-wasm-worker", layout.projectDirectory.dir("opfsWorker").asFile))
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

afterEvaluate {
    tasks.named("extractAndroidMainAnnotations") {
        dependsOn("kspAndroidMain")
    }
}

dependencies {

    androidRuntimeClasspath(libs.compose.ui.tooling)

    add("kspAndroid", libs.room3.compiler)
    add("kspJvm", libs.room3.compiler)
    add("kspIosArm64", libs.room3.compiler)
    add("kspIosSimulatorArm64", libs.room3.compiler)
    add("kspWasmJs", libs.room3.compiler)
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

compose {
    resources {
        publicResClass = true
        packageOfResClass = "dev.catbit.mosaic.client.generated.resources"
    }
}

composeStabilityAnalyzer {
    traceAll {
        enabled.set(true)            // default: false (opt-in)
        threshold.set(2)             // default: 2 — skips the initial-composition burst
        variants.set(listOf("debug")) // default: ["debug"] — never applies to release or tests
    }
}