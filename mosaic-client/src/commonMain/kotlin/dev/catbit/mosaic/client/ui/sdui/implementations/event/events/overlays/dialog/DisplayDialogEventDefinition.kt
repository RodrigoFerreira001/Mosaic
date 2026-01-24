package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.overlays.DisplayDialogEventModel

object DisplayDialogEventDefinition : EventDefinition<DisplayDialogEventModel> {
    override val eventModelClass = DisplayDialogEventModel::class
    override val eventRunner = DisplayDialogEventRunner
    override val eventHolderBuilder = DisplayDialogEventHolderBuilder
}