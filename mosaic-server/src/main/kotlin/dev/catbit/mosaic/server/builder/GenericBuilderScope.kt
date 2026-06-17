package dev.catbit.mosaic.server.builder

import dev.catbit.mosaic.core.extensions.immutableMapTo
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.server.builder.composition_local.BuildContext

abstract class GenericBuilderScope<Model, Builder : GenericBuilder<Model>> {

    private val builders = mutableListOf<Builder>()

    fun build(): SerializableImmutableList<Model> = builders.immutableMapTo { builder ->
        BuildContext.with(builder.compositionLocals) { builder.build() }
    }

    fun addBuilder(builder: Builder) {
        builders.add(builder.apply { compositionLocals = BuildContext.get() })
    }
}