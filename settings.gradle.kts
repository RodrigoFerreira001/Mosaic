rootProject.name = "Mosaic"

include(":mosaic-client")
include(":mosaic-server")
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