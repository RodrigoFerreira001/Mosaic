rootProject.name = "Mosaic"

include(":mosaic-client")
include(":mosaic-client-ksp")
include(":mosaic-server")
include(":mosaic-server-ksp")
include(":mosaic-core")
include(":sample-client")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}