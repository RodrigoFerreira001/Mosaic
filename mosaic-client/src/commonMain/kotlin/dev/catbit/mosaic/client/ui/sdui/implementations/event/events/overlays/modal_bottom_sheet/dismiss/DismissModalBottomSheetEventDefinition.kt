package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.modal_bottom_sheet.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DismissModalBottomSheetEventSchema

object DismissModalBottomSheetEventDefinition : EventDefinition<DismissModalBottomSheetEventSchema> {
    override val eventSchemaClass = DismissModalBottomSheetEventSchema::class
    override val eventRunner = DismissModalBottomSheetEventRunner
    override val eventHolderBuilder = DismissModalBottomSheetEventHolderBuilder
}