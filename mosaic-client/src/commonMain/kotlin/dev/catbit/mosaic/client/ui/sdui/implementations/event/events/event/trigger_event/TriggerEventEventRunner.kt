package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.runSafely

object TriggerEventEventRunner : EventRunner<TriggerEventEventSchema> {
    override fun EventRunningScope.runEvent(event: TriggerEventEventSchema) {
        runSafely(
            onError = {
                onTrigger(EventTriggers.onFailure(), data = it)
                logError(
                    tag = "TriggerEventEventRunner",
                    throwable = it
                )
            }
        ) {
            tilesEventDispatcher.getEventSchema(event.eventId)?.let { eventSchema ->
                runEventInline(eventSchema)
                onTrigger(EventTriggers.onSuccess())
            } ?: run {
                onTrigger(EventTriggers.onFailure())
                logError(
                    tag = "TriggerEventEventRunner",
                    throwable = Throwable("Event not found: ${event.eventId}")
                )
            }
        }
    }
}
