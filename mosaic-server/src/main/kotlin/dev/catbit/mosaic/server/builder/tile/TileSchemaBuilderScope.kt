package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider

class TileSchemaBuilderScope private constructor() : GenericBuilderScope<TileSchema, TileSchemaBuilder<*>>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = TileSchemaBuilderScope().apply { pushLocals(compositionLocals) }
    }
}