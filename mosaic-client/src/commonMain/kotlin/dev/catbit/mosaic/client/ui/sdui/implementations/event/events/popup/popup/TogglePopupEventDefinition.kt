package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.popup.popup

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.popup.TogglePopupEventSchema

object TogglePopupEventDefinition : EventDefinition<TogglePopupEventSchema> {
    override val eventSchemaClass = TogglePopupEventSchema::class
    override val eventRunner = TogglePopupEventRunner
    override val eventHolderBuilder = TogglePopupEventHolderBuilder
}
