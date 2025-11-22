plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    dependencies {
        implementation(libs.kotlinpoet)
        implementation(libs.kotlinpoet.ksp)
        implementation(libs.ksp)
    }
}