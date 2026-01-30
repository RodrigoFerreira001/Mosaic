package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema

object AddTilesEventDefinition : EventDefinition<AddTilesEventSchema> {
    override val eventSchemaClass = AddTilesEventSchema::class
    override val eventRunner = AddTilesEventRunner
    override val eventHolderBuilder = AddTilesEventHolderBuilder
}