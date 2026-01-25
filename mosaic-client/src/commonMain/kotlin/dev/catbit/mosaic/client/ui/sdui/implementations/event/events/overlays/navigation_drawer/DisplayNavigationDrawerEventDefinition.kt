package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventModel

object DisplayNavigationDrawerEventDefinition : EventDefinition<DisplayNavigationDrawerEventModel> {
    override val eventModelClass = DisplayNavigationDrawerEventModel::class
    override val eventRunner = DisplayNavigationDrawerEventRunner
    override val eventHolderBuilder = DisplayNavigationDrawerEventHolderBuilder
}