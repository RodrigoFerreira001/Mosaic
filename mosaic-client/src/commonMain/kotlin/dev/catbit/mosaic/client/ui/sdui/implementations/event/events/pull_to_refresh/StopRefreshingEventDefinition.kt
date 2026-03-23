package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.pull_to_refresh

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh.StopRefreshingEventSchema

object StopRefreshingEventDefinition : EventDefinition<StopRefreshingEventSchema> {
    override val eventSchemaClass = StopRefreshingEventSchema::class
    override val eventRunner = StopRefreshingEventRunner
    override val eventHolderBuilder = StopRefreshingEventHolderBuilder
}