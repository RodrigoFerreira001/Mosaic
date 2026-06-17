package dev.catbit.mosaic.server.builder

import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider

abstract class GenericBuilder<out T> {
    internal var compositionLocals = emptyMap<CompositionLocal<*>, ValueProvider<*>>()

    abstract fun build(): T
}
