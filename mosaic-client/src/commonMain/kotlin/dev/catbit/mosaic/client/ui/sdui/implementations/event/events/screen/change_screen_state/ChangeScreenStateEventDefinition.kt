package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.change_screen_state

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema

object ChangeScreenStateEventDefinition : EventDefinition<ChangeScreenStateEventSchema> {
    override val eventSchemaClass = ChangeScreenStateEventSchema::class
    override val eventRunner = ChangeScreenStateEventRunner
    override val eventHolderBuilder = ChangeScreenStateEventHolderBuilder
}
