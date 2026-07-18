package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.tooltip

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.tooltip.TooltipTileSchema

object TooltipTileDefinition : TileDefinition<TooltipTileSchema> {
    override val tileSchemaClass = TooltipTileSchema::class
    override val tileRenderer = TooltipTileRenderer
    override val tileHolderBuilder = TooltipTileHolderBuilder
}
