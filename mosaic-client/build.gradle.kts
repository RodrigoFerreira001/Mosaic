@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.kotlin.dsl.android
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.ksp)
}

group = "dev.catbit.mosaic-core"
version = "1.0.0"

kotlin {
    applyDefaultHierarchyTemplate()

    android {
        namespace = "dev.catbit.mosaic-client"
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

            // Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":mosaic-client-ksp"))
    add("kspAndroid", project(":mosaic-client-ksp"))
    add("kspJvm", project(":mosaic-client-ksp"))
    add("kspIosX64", project(":mosaic-client-ksp"))
    add("kspIosArm64", project(":mosaic-client-ksp"))
    add("kspIosSimulatorArm64", project(":mosaic-client-ksp"))
    add("kspWasmJs", project(":mosaic-client-ksp"))
}

afterEvaluate {
    tasks.named("extractAndroidMainAnnotations") {
        dependsOn(tasks.named("kspAndroidMain"))
    }
}