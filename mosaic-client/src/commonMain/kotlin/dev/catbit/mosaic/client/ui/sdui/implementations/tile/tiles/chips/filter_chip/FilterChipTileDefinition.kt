package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.filter_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.FilterChipTileSchema

object FilterChipTileDefinition : TileDefinition<FilterChipTileSchema> {
    override val tileSchemaClass = FilterChipTileSchema::class
    override val tileRenderer = FilterChipTileRenderer
    override val tileHolderBuilder = FilterChipTileHolderBuilder
}
