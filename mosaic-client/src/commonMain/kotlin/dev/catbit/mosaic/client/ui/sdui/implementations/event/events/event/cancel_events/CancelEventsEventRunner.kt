package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.cancel_events

import dev.catbit.mosaic.client.ui.sdui.foundation.events.CancellableEventsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.CancelEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object CancelEventsEventRunner : EventRunner<CancelEventsEventSchema> {

    override suspend fun EventRunningScope.runEvent(
        event: CancelEventsEventSchema
    ) {
        with(event) {
            get<CancellableEventsHolder>().cancelEvents(
                cancellableEventId = cancellableEventId
            )
                .onSuccess {
                    onTrigger(EventTriggers.onSuccess())
                }
                .onFailure { throwable ->
                    onTrigger(EventTriggers.onFailure(), data = throwable)
                    logError(tag = "CancelEventsEventRunner", throwable = throwable)
                }
        }
    }
}
