plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

application {
    mainClass = "dev.catbit.mosaic.ApplicationKt"
}

dependencies {
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
