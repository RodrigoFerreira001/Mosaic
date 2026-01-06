package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorHolder
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateEventModel

object NavigateEventRunner : EventRunner<NavigateEventModel> {

    override fun EventRunningScope.runEvent(event: NavigateEventModel) {
        NavigatorHolder[event.navigatorId]?.add(
            ScreenNavKey(
                id = event.destination,
                navigationData = event.data
            )
        )
    }
}