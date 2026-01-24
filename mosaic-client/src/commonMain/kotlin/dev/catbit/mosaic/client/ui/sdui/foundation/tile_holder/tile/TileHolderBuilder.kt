package dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.core.data.tile.TileModel

interface TileHolderBuilder<out T: TileModel, H : TileHolder<@UnsafeVariance T>> {
    fun BuilderScope.build(tileModel: @UnsafeVariance T): H
}