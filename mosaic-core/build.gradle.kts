@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlin.serialization)
}

group = "dev.catbit"
version = "1.0.0"

kotlin {
    jvm()
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
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()
    wasmJs {
        browser()
        nodejs()
    }
    js(IR) {
        browser()
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            // Koin
            implementation(libs.koin.core)

            // Kotlin serialization
            implementation(libs.kotlinx.serialization.json)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Kotlinx Datetime
            implementation(libs.kotlinx.datetime)
        }
        androidMain.dependencies {
            // Coroutines
            implementation(libs.kotlinx.coroutines.android)
        }
        jvmMain.dependencies {
            // Coroutines
            implementation(libs.kotlinx.coroutines.swing)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}