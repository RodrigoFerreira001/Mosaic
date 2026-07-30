package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_cancellable_events

import dev.catbit.mosaic.client.ui.sdui.foundation.events.CancellableEventsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunCancellableEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object RunCancellableEventsEventRunner : EventRunner<RunCancellableEventsEventSchema> {

    override suspend fun EventRunningScope.runEvent(
        event: RunCancellableEventsEventSchema
    ) {
        with(event) {
            events?.let {
                get<CancellableEventsHolder>().runEvents(
                    cancellableEventId = cancellableEventId
                ) {
                    this@runEvent.runEventsInline(it)
                }
                onTrigger(EventTriggers.onSuccess())
            } ?: run {
                onTrigger(EventTriggers.onFailure())
            }
        }
    }
}
