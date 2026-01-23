package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.poppingUpTo
import dev.catbit.mosaic.core.data.event.events.navigation.NavigateEventModel

object NavigateEventRunner : EventRunner<NavigateEventModel> {

    override suspend fun EventRunningScope.runEvent(event: NavigateEventModel) {
        NavigatorHolder[event.navigatorId]?.navigate(
            destination = event.destination,
            navigationData = event.data.orEmpty() + incomingData.asMapAny().orEmpty(),
            poppingUpTo = event.popUpTo?.let { popUpTo ->
                poppingUpTo(
                    target = popUpTo.destination,
                    inclusive = popUpTo.inclusive
                )
            }
        )
    }
}