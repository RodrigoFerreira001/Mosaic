package dev.catbit.mosaic.server.builder

import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider

abstract class GenericBuilderScope<Model, Builder : GenericBuilder<Model>> {

    private val builders = mutableListOf<Builder>()
    internal fun build() = builders.map { it.build() }
    internal fun addBuilder(builder: Builder) {
        builders.add(
            builder.apply { compositionLocals = this@GenericBuilderScope.compositionLocals.toMap() }
        )
    }

    private val compositionLocals = mutableMapOf<CompositionLocal<*>, ValueProvider<*>>()

    internal fun snapshotLocals(): Map<CompositionLocal<*>, ValueProvider<*>> = compositionLocals.toMap()

    internal fun pushLocals(locals: Map<CompositionLocal<*>, ValueProvider<*>>) {
        compositionLocals.putAll(locals)
    }

    internal fun restoreLocals(snapshot: Map<CompositionLocal<*>, ValueProvider<*>>) {
        compositionLocals.clear()
        compositionLocals.putAll(snapshot)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> CompositionLocal<T>.current(): T = (compositionLocals[this]?.provide() as? T) ?: defaultValue()
}