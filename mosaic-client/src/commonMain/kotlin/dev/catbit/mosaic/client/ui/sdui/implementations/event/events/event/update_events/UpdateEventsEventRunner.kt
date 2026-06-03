package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.update_events

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object UpdateEventsEventRunner : EventRunner<UpdateEventsEventSchema> {
    override fun EventRunningScope.runEvent(event: UpdateEventsEventSchema) {

        var errorHappened = false

        event.updates.forEach { update ->
            tilesEventDispatcher.updateEventHolder(
                eventId = update.eventId,
                data = update.data,
                onError = { errorHappened = true },
                onSuccess = {}
            )
        }
        if (errorHappened) {
            onTrigger(EventTriggers.onFailure())
            logError(
                tag = "UpdateEventsEventRunner",
                throwable = Throwable("One or more event updates failed")
            )
        } else {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
