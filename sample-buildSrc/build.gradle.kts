plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("mosaicBuildConfig") {
            id = "mosaic-build-config"
            implementationClass = "dev.catbit.mosaic.sample.buildconfig.BuildConfigPlugin"
        }
    }
}
