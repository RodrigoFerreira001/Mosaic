package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.card

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CardTileSchema

object CardTileDefinition : TileDefinition<CardTileSchema> {
    override val tileSchemaClass = CardTileSchema::class
    override val tileRenderer = CardTileRenderer
    override val tileHolderBuilder = CardTileHolderBuilder
}
