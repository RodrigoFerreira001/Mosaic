package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_events

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.runSafely

object RunEventsEventRunner : EventRunner<RunEventsEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: RunEventsEventSchema) {
        var anyErrorOccurred = false

        event.events?.forEach { event ->
            runSafely(
                onError = {
                    anyErrorOccurred = true
                    logError(throwable = it, tag = "RunEventsEventRunner")
                }
            ) {
                runEventInline(
                    eventSchema = event,
                    data = incomingData
                )
            }
        }

        if (anyErrorOccurred) {
            onTrigger(EventTriggers.onFailure())
        } else {
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
