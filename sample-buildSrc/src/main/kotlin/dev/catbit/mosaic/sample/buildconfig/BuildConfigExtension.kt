package dev.catbit.mosaic.sample.buildconfig

import org.gradle.api.provider.Property

interface BuildConfigExtension {
    val packageName: Property<String>
}
