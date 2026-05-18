plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlin.serialization)
}

mavenPublishing {
    coordinates(
        groupId = "dev.catbit",
        artifactId = "mosaic-server",
        version = "1.0.0"
    )
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    // Mosaic core
    implementation(projects.mosaicCore)

    // Koin
    implementation(libs.koin.core)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // Kotlinx Datetime
    implementation(libs.kotlinx.datetime)
}