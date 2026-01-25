package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.overlays.bottom_sheet.DisplayBottomSheetEventModel

object DisplayBottomSheetEventDefinition : EventDefinition<DisplayBottomSheetEventModel> {
    override val eventModelClass = DisplayBottomSheetEventModel::class
    override val eventRunner = DisplayBottomSheetEventRunner
    override val eventHolderBuilder = DisplayBottomSheetEventHolderBuilder
}