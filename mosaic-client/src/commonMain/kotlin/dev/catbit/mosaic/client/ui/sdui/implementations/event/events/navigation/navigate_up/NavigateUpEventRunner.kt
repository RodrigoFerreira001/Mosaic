package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorHolder
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateUpEventModel

object NavigateUpEventRunner : EventRunner<NavigateUpEventModel> {

    override suspend fun EventRunningScope.runEvent(event: NavigateUpEventModel) {
        NavigatorHolder[event.navigatorId]?.goBack()
    }
}