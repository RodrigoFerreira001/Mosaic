plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlin.serialization)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "dev.catbit",
        artifactId = "mosaic-server",
        version = libs.versions.mosaic.get()
    )

    pom {
        name = "Mosaic Server"
        description = "Type-safe Kotlin DSL for building Server-Driven UI screens with the Mosaic framework."
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
    jvmToolchain(11)
}

dependencies {
    // Test
    testImplementation(libs.kotlin.test)

    // Mosaic core
    implementation(projects.mosaicCore)

    // Koin
    implementation(libs.koin.core)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // Kotlinx Datetime
    implementation(libs.kotlinx.datetime)

    // Kotlinx Collections Immutable
    implementation(libs.kotlinx.collections.immutable)
}