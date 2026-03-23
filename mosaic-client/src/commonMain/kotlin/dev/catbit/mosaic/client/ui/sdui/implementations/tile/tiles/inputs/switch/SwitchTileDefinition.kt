package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.switch

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.SwitchTileSchema

object SwitchTileDefinition : TileDefinition<SwitchTileSchema> {
    override val tileSchemaClass = SwitchTileSchema::class
    override val tileRenderer = SwitchTileRenderer
    override val tileHolderBuilder = SwitchTileHolderBuilder
}
