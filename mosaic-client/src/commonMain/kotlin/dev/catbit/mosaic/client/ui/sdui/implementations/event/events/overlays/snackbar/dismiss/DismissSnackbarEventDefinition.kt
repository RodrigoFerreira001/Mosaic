package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DismissSnackbarEventSchema

object DismissSnackbarEventDefinition : EventDefinition<DismissSnackbarEventSchema> {
    override val eventSchemaClass = DismissSnackbarEventSchema::class
    override val eventRunner = DismissSnackbarEventRunner
    override val eventHolderBuilder = DismissSnackbarEventHolderBuilder
}
