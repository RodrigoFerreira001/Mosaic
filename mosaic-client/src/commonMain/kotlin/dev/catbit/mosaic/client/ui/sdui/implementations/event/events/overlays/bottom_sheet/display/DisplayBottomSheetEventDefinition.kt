package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DisplayBottomSheetEventSchema

object DisplayBottomSheetEventDefinition : EventDefinition<DisplayBottomSheetEventSchema> {
    override val eventSchemaClass = DisplayBottomSheetEventSchema::class
    override val eventRunner = DisplayBottomSheetEventRunner
    override val eventHolderBuilder = DisplayBottomSheetEventHolderBuilder
}