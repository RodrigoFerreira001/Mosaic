package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

interface TileHolderBuilder<out T: TileSchema, H : TileHolder<@UnsafeVariance T>> {
    fun BuilderScope.build(tileModel: @UnsafeVariance T): H
}