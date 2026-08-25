plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    id("mosaic-build-config")
}

application {
    mainClass = "dev.catbit.mosaic.sample.server.ApplicationKt"
}

buildConfig {
    packageName.set("dev.catbit.mosaic.sample.server")
}

kotlin {
    // mosaic-core's jvm target compiles at JvmTarget.JVM_21 (unlike its android target, pinned
    // at 11) — sample-server has to run on a JDK that can load that bytecode, and the Dockerfile
    // already runs on eclipse-temurin:21-jre for the same reason. Keep both in sync.
    jvmToolchain(21)
}

sourceSets {
    main {
        kotlin.srcDir(
            tasks.named("generateBuildConfig").map {
                layout.buildDirectory.dir("generated/buildconfig")
            }
        )
    }
}

dependencies {
    implementation(projects.sampleCore)
    implementation(projects.mosaicCore)
    implementation(projects.mosaicServer)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.network.tls.certificates)
    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.server.cors)
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.collections.immutable)
}
