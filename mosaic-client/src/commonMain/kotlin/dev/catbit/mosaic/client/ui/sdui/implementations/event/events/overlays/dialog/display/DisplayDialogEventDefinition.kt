package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.display

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema

object DisplayDialogEventDefinition : EventDefinition<DisplayDialogEventSchema> {
    override val eventSchemaClass = DisplayDialogEventSchema::class
    override val eventRunner = DisplayDialogEventRunner
    override val eventHolderBuilder = DisplayDialogEventHolderBuilder
}