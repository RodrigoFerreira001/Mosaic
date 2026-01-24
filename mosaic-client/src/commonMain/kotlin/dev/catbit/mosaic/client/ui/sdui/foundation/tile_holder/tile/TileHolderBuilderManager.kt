package dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlin.reflect.KClass

class TileHolderBuilderManager(
    private val builders: Map<KClass<out TileModel>, TileHolderBuilder<*, *>>
) {
    fun BuilderScope.build(tileModel: TileModel) = builders[tileModel::class]?.let { builder ->
        with(builder) { build(tileModel) }
    } ?: throw IllegalArgumentException("Couldn't find a builder for $tileModel")
}