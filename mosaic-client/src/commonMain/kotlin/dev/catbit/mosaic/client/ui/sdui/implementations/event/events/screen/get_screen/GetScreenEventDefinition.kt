package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.get_screen

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema

object GetScreenEventDefinition : EventDefinition<GetScreenEventSchema> {
    override val eventSchemaClass = GetScreenEventSchema::class
    override val eventRunner = GetScreenEventRunner
    override val eventHolderBuilder = GetScreenEventHolderBuilder
}
