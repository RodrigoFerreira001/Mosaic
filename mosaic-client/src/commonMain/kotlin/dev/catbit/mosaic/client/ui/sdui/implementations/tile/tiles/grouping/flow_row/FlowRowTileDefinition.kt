package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.flow_row

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlowRowTileSchema

object FlowRowTileDefinition : TileDefinition<FlowRowTileSchema> {
    override val tileSchemaClass = FlowRowTileSchema::class
    override val tileRenderer = FlowRowTileRenderer
    override val tileHolderBuilder = FlowRowTileHolderBuilder
}
