package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema

object UpdateTilesEventDefinition : EventDefinition<UpdateTilesEventSchema> {
    override val eventSchemaClass = UpdateTilesEventSchema::class
    override val eventRunner = UpdateTilesEventRunner
    override val eventHolderBuilder = UpdateTilesEventHolderBuilder
}
