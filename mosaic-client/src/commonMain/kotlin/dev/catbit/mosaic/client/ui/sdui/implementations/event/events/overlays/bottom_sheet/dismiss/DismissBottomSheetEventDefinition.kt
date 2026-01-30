package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DismissBottomSheetEventSchema

object DismissBottomSheetEventDefinition : EventDefinition<DismissBottomSheetEventSchema> {
    override val eventSchemaClass = DismissBottomSheetEventSchema::class
    override val eventRunner = DismissBottomSheetEventRunner
    override val eventHolderBuilder = DismissBottomSheetEventHolderBuilder
}