package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.overlays.DismissDialogEventModel

object DismissDialogEventDefinition : EventDefinition<DismissDialogEventModel> {
    override val eventModelClass = DismissDialogEventModel::class
    override val eventRunner = DismissDialogEventRunner
    override val eventHolderBuilder = DismissDialogEventHolderBuilder
}