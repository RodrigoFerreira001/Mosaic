package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.WipeTilesEventSchema

object WipeTilesEventDefinition : EventDefinition<WipeTilesEventSchema> {
    override val eventSchemaClass = WipeTilesEventSchema::class
    override val eventRunner = WipeTilesEventRunner
    override val eventHolderBuilder = WipeTilesEventHolderBuilder
}