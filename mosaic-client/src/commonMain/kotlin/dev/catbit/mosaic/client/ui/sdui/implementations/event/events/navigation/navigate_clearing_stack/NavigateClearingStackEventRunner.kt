package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_clearing_stack

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorsHolder
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateClearingStackEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object NavigateClearingStackEventRunner : EventRunner<NavigateClearingStackEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: NavigateClearingStackEventSchema) {
        get<NavigatorsHolder>()[event.navigatorId]?.navigateClearingStack(
            destination = event.destination,
            // Navigation args are never null by design, so nulls from incomingData are dropped here.
            navigationData = incomingData.asMapAny().orEmpty().filterValues { it != null }
                .mapValues { it.value as Any } + event.data.orEmpty(),
            launchSingleTop = event.launchSingleTop
        )?.also {
            onTrigger(EventTriggers.onSuccess())
        } ?: run {
            onTrigger(EventTriggers.onFailure())
            logError(
                tag = "NavigateClearingStackEventRunner",
                throwable = Throwable("Navigation failed")
            )
        }
    }
}
