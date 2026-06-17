package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.poppingUpTo
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object NavigateEventRunner : EventRunner<NavigateEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: NavigateEventSchema) {
        get<NavigatorsHolder>()[event.navigatorId]?.navigate(
            destination = event.destination,
            navigationData = incomingData.asMapAny().orEmpty() + event.data.orEmpty(),
            poppingUpTo = event.popUpTo?.let { popUpTo ->
                poppingUpTo(
                    target = popUpTo.destination,
                    inclusive = popUpTo.inclusive
                )
            }
        )?.also {
            onTrigger(EventTriggers.onSuccess())
        } ?: run {
            onTrigger(EventTriggers.onFailure())
            logError(
                tag = "NavigateEventRunner",
                throwable = Throwable("Navigation failed")
            )
        }
    }
}