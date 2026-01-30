package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlin.reflect.KClass

class TileHolderBuilderManager(
    private val builders: Map<KClass<out TileSchema>, TileHolderBuilder<*, *>>
) {
    fun BuilderScope.build(tileSchema: TileSchema) = builders[tileSchema::class]?.let { builder ->
        with(builder) { build(tileSchema) }
    } ?: throw IllegalArgumentException("Couldn't find a builder for $tileSchema")
}