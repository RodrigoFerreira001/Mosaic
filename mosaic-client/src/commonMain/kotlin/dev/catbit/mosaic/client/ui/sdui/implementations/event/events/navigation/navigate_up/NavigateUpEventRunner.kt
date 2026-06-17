package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorsHolder
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object NavigateUpEventRunner : EventRunner<NavigateUpEventSchema> {

    override suspend fun EventRunningScope.runEvent(event: NavigateUpEventSchema) {
        get<NavigatorsHolder>()[event.navigatorId]?.goBack()
            ?.also {
                onTrigger(EventTriggers.onSuccess())
            } ?: run {
            onTrigger(EventTriggers.onFailure())
            logError(
                tag = "NavigateUpEventRunner",
                throwable = Throwable("NavigateUp failed")
            )
        }
    }
}