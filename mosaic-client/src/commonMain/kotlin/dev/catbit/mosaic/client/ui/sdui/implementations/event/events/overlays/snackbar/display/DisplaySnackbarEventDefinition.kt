package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.display

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DisplaySnackbarEventSchema

object DisplaySnackbarEventDefinition : EventDefinition<DisplaySnackbarEventSchema> {
    override val eventSchemaClass = DisplaySnackbarEventSchema::class
    override val eventRunner = DisplaySnackbarEventRunner
    override val eventHolderBuilder = DisplaySnackbarEventHolderBuilder
}
