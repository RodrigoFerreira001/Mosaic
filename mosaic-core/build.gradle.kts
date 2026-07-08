@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlin.serialization)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "dev.catbit",
        artifactId = "mosaic-core",
        version = libs.versions.mosaic.get()
    )

    pom {
        name = "Mosaic Core"
        description = "Shared Schemas, polymorphic serialization and base contracts for the Mosaic Server-Driven UI framework."
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
        namespace = "dev.catbit.mosaic"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions.jvmTarget = JvmTarget.JVM_11
            }
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm {
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions.jvmTarget = JvmTarget.JVM_21
            }
        }
    }

    wasmJs {
        browser()
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            // Compose
            implementation(libs.compose.runtime)

            // Koin
            implementation(libs.koin.core)

            // Kotlin serialization
            implementation(libs.kotlinx.serialization.json)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Kotlinx Datetime
            implementation(libs.kotlinx.datetime)

            // Kotlinx Collections Immutable
            implementation(libs.kotlinx.collections.immutable)
        }
        androidMain.dependencies {
            // Coroutines
            implementation(libs.kotlinx.coroutines.android)
        }
        jvmMain.dependencies {
            // Coroutines
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}