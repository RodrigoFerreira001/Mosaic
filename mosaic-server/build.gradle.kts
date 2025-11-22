@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
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
            // Mosaic core
            implementation(project(":mosaic-core"))

            // Koin
            implementation(libs.koin.core)

            // Kotlin serialization
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":mosaic-server-ksp"))
    add("kspAndroid", project(":mosaic-server-ksp"))
    add("kspJvm", project(":mosaic-server-ksp"))
    add("kspIosX64", project(":mosaic-server-ksp"))
    add("kspIosArm64", project(":mosaic-server-ksp"))
    add("kspIosSimulatorArm64", project(":mosaic-server-ksp"))
    add("kspWasmJs", project(":mosaic-server-ksp"))
}

afterEvaluate {
    tasks.named("extractAndroidMainAnnotations") {
        dependsOn(tasks.named("kspAndroidMain"))
    }
}