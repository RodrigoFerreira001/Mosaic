package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.NavigateEventModel

class NavigateEventRunner : EventRunner<NavigateEventModel> {

    override fun EventRunningScope.runEvent(event: NavigateEventModel) {
        incomingData?.let { println(it) }
        print("Rodou")
    }
}