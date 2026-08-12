package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.modal_bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DisplayModalBottomSheetEventSchema

object DisplayModalBottomSheetEventDefinition : EventDefinition<DisplayModalBottomSheetEventSchema> {
    override val eventSchemaClass = DisplayModalBottomSheetEventSchema::class
    override val eventRunner = DisplayModalBottomSheetEventRunner
    override val eventHolderBuilder = DisplayModalBottomSheetEventHolderBuilder
}