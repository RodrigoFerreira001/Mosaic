package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema

object DismissDialogEventDefinition : EventDefinition<DismissDialogEventSchema> {
    override val eventSchemaClass = DismissDialogEventSchema::class
    override val eventRunner = DismissDialogEventRunner
    override val eventHolderBuilder = DismissDialogEventHolderBuilder
}