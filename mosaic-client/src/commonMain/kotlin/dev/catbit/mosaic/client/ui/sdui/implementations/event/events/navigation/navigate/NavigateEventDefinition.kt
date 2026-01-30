package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema

object NavigateEventDefinition : EventDefinition<NavigateEventSchema> {
    override val eventSchemaClass = NavigateEventSchema::class
    override val eventRunner = NavigateEventRunner
    override val eventHolderBuilder = NavigateEventHolderBuilder
}