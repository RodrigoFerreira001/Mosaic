package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateUpEventModel

object NavigateUpEventDefinition : EventDefinition<NavigateUpEventModel> {
    override val eventModelClass = NavigateUpEventModel::class
    override val eventRunner = NavigateUpEventRunner
    override val eventHolderBuilder = NavigateUpEventHolderBuilder
}