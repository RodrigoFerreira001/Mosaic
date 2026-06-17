package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_events

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunEventsEventSchema
import dev.catbit.mosaic.core.extensions.runSafely

object RunEventsEventRunner : EventRunner<RunEventsEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: RunEventsEventSchema) {
        event.events?.forEach { event ->
            runSafely {
                runEventInline(
                    eventSchema = event,
                    data = incomingData
                )
            }
        }
    }
}
