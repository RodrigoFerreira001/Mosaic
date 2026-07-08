package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.popup

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.popup.PopupTileSchema

object PopupTileDefinition : TileDefinition<PopupTileSchema> {
    override val tileSchemaClass = PopupTileSchema::class
    override val tileRenderer = PopupTileRenderer
    override val tileHolderBuilder = PopupTileHolderBuilder
}
