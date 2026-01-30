package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema

object NavigateUpEventDefinition : EventDefinition<NavigateUpEventSchema> {
    override val eventSchemaClass = NavigateUpEventSchema::class
    override val eventRunner = NavigateUpEventRunner
    override val eventHolderBuilder = NavigateUpEventHolderBuilder
}