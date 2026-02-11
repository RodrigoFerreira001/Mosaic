package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.refresh_screen

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema

object RefreshScreenEventDefinition : EventDefinition<RefreshScreenEventSchema> {
    override val eventSchemaClass = RefreshScreenEventSchema::class
    override val eventRunner = RefreshScreenEventRunner
    override val eventHolderBuilder = RefreshScreenEventHolderBuilder
}
