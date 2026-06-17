package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.check_if_tile_contains_children

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.CheckIfTileContainsChildrenEventSchema

object CheckIfTileContainsChildrenEventDefinition : EventDefinition<CheckIfTileContainsChildrenEventSchema> {
    override val eventSchemaClass = CheckIfTileContainsChildrenEventSchema::class
    override val eventRunner = CheckIfTileContainsChildrenEventRunner
    override val eventHolderBuilder = CheckIfTileContainsChildrenEventHolderBuilder
}
