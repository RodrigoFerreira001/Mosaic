plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

group = "dev.catbit"
version = "1.0.0"

application {
    mainClass = "dev.catbit.mosaic.ApplicationKt"
}

dependencies {
    implementation(project(":mosaic-core"))
    implementation(project(":mosaic-server"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.logback.classic)
    testImplementation("io.ktor:ktor-server-test-host-jvm:3.3.3")
}
