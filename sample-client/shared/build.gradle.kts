@file:OptIn(ExperimentalWasmDsl::class)
@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "dev.catbit.mosaic.sample.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    jvm()

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            // Mosaic
            implementation(projects.sampleCore)
            implementation(projects.mosaicCore)
            implementation(projects.mosaicClient)

            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)

            // Adaptive
            implementation(libs.compose.material3.adaptative)
            implementation(libs.compose.viewmodel)

            // Navigation3
            implementation(libs.compose.navigation3.ui)
            implementation(libs.compose.navigation3.adaptiveNavigation3)
            implementation(libs.compose.navigation3.viewmodelNavigation3)

            // Koin
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Kotlinx Collections Immutable
            implementation(libs.kotlinx.collections.immutable)

            // Kotlin serialization
            implementation(libs.kotlinx.serialization.json)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Shimmer
            implementation(libs.shimmer)

            // Kotlinx Datetime
            implementation(libs.kotlinx.datetime)

            // Markdown reder
            implementation(libs.markdown.render.core)
            implementation(libs.markdown.render.m3)
            implementation(libs.markdown.render.code)
        }
    }
}
