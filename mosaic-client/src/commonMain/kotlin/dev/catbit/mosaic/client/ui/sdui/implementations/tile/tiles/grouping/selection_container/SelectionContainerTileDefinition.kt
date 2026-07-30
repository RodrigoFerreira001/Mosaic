package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.selection_container

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.SelectionContainerTileSchema

object SelectionContainerTileDefinition : TileDefinition<SelectionContainerTileSchema> {
    override val tileSchemaClass = SelectionContainerTileSchema::class
    override val tileRenderer = SelectionContainerTileRenderer
    override val tileHolderBuilder = SelectionContainerTileHolderBuilder
}
