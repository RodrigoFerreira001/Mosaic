package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.update_events

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object UpdateEventsEventRunner : EventRunner<UpdateEventsEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: UpdateEventsEventSchema) {

        var anyErrorOccurred = false

        event.updates.forEach { update ->
            tilesEventDispatcher.updateEventHolder(
                eventId = update.eventId,
                data = update.data,
            ).onFailure {
                anyErrorOccurred = true
            }
        }

        if (anyErrorOccurred) {
            onTrigger(EventTriggers.onFailure())
        } else {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
