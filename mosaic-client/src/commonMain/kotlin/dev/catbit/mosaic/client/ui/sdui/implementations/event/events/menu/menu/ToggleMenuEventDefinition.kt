package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.menu.ToggleMenuEventModel

object ToggleMenuEventDefinition : EventDefinition<ToggleMenuEventModel> {
    override val eventModelClass = ToggleMenuEventModel::class
    override val eventRunner = ToggleMenuEventRunner
}