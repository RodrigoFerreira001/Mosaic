package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema

object ReplaceTilesEventDefinition : EventDefinition<ReplaceTilesEventSchema> {
    override val eventSchemaClass = ReplaceTilesEventSchema::class
    override val eventRunner = ReplaceTilesEventRunner
    override val eventHolderBuilder = ReplaceTilesEventHolderBuilder
}