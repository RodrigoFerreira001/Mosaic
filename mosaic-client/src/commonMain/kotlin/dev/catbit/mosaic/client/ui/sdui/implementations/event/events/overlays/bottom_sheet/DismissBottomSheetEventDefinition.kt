package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.overlays.DismissBottomSheetEventModel

object DismissBottomSheetEventDefinition : EventDefinition<DismissBottomSheetEventModel> {
    override val eventModelClass = DismissBottomSheetEventModel::class
    override val eventRunner = DismissBottomSheetEventRunner
    override val eventHolderBuilder = DismissBottomSheetEventHolderBuilder
}