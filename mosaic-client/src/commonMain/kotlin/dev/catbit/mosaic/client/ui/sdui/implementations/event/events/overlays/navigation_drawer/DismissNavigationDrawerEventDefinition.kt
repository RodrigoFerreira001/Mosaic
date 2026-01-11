package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.event.events.overlays.DismissNavigationDrawerEventModel

object DismissNavigationDrawerEventDefinition : EventDefinition<DismissNavigationDrawerEventModel> {
    override val eventModelClass = DismissNavigationDrawerEventModel::class
    override val eventRunner = DismissNavigationDrawerEventRunner
}