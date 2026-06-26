package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.simple_text

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema

object SimpleTextTileHolderBuilder : TileHolderBuilder<SimpleTextTileSchema, SimpleTextTileHolder> {

    override fun BuilderScope.build(
        tileModel: SimpleTextTileSchema
    ): SimpleTextTileHolder = with(tileModel) {
        SimpleTextTileHolder(
            id = id,
            tile = this,
            events = events.buildEventHolders()
        )
    }
}
