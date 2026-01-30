package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.menu.ToggleMenuEventSchema

object ToggleMenuEventDefinition : EventDefinition<ToggleMenuEventSchema> {
    override val eventSchemaClass = ToggleMenuEventSchema::class
    override val eventRunner = ToggleMenuEventRunner
    override val eventHolderBuilder = ToggleMenuEventHolderBuilder
}