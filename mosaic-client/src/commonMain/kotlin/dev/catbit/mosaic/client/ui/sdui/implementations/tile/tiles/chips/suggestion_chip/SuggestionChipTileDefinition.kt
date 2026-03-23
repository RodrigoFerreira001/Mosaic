package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.suggestion_chip

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.SuggestionChipTileSchema

object SuggestionChipTileDefinition : TileDefinition<SuggestionChipTileSchema> {
    override val tileSchemaClass = SuggestionChipTileSchema::class
    override val tileRenderer = SuggestionChipTileRenderer
    override val tileHolderBuilder = SuggestionChipTileHolderBuilder
}
