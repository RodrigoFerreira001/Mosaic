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
    coordinates(
        groupId = "dev.catbit",
        artifactId = "mosaic-core",
        version = "1.0.0"
    )
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