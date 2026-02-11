package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.carousel

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CarouselTileSchema

object CarouselTileDefinition : TileDefinition<CarouselTileSchema> {
    override val tileSchemaClass = CarouselTileSchema::class
    override val tileRenderer = CarouselTileRenderer
    override val tileHolderBuilder = CarouselTileHolderBuilder
}
