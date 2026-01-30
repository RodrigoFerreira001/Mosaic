package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.display

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventSchema

object DisplayNavigationDrawerEventDefinition : EventDefinition<DisplayNavigationDrawerEventSchema> {
    override val eventSchemaClass = DisplayNavigationDrawerEventSchema::class
    override val eventRunner = DisplayNavigationDrawerEventRunner
    override val eventHolderBuilder = DisplayNavigationDrawerEventHolderBuilder
}