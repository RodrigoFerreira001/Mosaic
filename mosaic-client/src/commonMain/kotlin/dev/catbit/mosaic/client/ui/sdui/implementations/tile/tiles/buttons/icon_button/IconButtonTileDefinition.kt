package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.icon_button

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.IconButtonTileSchema

object IconButtonTileDefinition : TileDefinition<IconButtonTileSchema> {
    override val tileSchemaClass = IconButtonTileSchema::class
    override val tileRenderer = IconButtonTileRenderer
    override val tileHolderBuilder = IconButtonTileHolderBuilder
}
