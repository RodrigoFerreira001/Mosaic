package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DismissNavigationDrawerEventSchema

object DismissNavigationDrawerEventDefinition : EventDefinition<DismissNavigationDrawerEventSchema> {
    override val eventSchemaClass = DismissNavigationDrawerEventSchema::class
    override val eventRunner = DismissNavigationDrawerEventRunner
    override val eventHolderBuilder = DismissNavigationDrawerEventHolderBuilder
}