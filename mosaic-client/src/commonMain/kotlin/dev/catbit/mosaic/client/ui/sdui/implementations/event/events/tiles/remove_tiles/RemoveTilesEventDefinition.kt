package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema

object RemoveTilesEventDefinition : EventDefinition<RemoveTilesEventSchema> {
    override val eventSchemaClass = RemoveTilesEventSchema::class
    override val eventRunner = RemoveTilesEventRunner
    override val eventHolderBuilder = RemoveTilesEventHolderBuilder
}