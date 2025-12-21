package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.NavigateEventModel

object NavigateEventDefinition : EventDefinition<NavigateEventModel> {
    override val eventModelClass = NavigateEventModel::class
    override val eventRunner = NavigateEventRunner()
}