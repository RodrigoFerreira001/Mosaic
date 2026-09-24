package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.theme.set_color_mode

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetColorModeEventSchema

object SetColorModeEventDefinition : EventDefinition<SetColorModeEventSchema> {
    override val eventSchemaClass = SetColorModeEventSchema::class
    override val eventRunner = SetColorModeEventRunner
    override val eventHolderBuilder = SetColorModeEventHolderBuilder
}
