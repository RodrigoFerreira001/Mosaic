package dev.catbit.mosaic.sample.client.sdui.tiles.code.code_viewer

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilder
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema

object CodeViewerTileHolderBuilder : TileHolderBuilder<CodeViewerTileSchema, CodeViewerTileHolder> {

    override fun BuilderScope.build(
        tileModel: CodeViewerTileSchema
    ): CodeViewerTileHolder = with(tileModel) {
        CodeViewerTileHolder(
            id = id,
            tile = tileModel,
            events = events.buildEventHolders()
        )
    }
}
