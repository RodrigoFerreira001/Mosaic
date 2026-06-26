package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.dropdown_list

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema

object DropdownListTileDefinition : TileDefinition<DropdownListTileSchema> {
    override val tileSchemaClass = DropdownListTileSchema::class
    override val tileRenderer = DropdownListTileRenderer
    override val tileHolderBuilder = DropdownListTileHolderBuilder
}
