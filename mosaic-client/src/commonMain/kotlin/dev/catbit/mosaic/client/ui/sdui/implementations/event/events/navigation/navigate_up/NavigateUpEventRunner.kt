package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorHolder
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema

object NavigateUpEventRunner : EventRunner<NavigateUpEventSchema> {

    override fun EventRunningScope.runEvent(event: NavigateUpEventSchema) {
        NavigatorHolder[event.navigatorId]?.goBack()
    }
}