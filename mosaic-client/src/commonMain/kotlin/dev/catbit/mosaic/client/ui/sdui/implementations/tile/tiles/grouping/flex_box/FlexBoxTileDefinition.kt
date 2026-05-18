package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.flex_box

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema

object FlexBoxTileDefinition : TileDefinition<FlexBoxTileSchema> {
    override val tileSchemaClass = FlexBoxTileSchema::class
    override val tileRenderer = FlexBoxTileRenderer
    override val tileHolderBuilder = FlexBoxTileHolderBuilder
}
