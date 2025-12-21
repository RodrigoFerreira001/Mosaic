package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.event.EventModel

class EventRunnerManager(
    private val eventRunners: List<EventRunner<*>>
) {
    fun EventRunningScope.runEvent(event: EventModel) {
        try {
            with(eventRunners.first { it.canRun(event) }) { runEvent(event) }
        } catch (e: Throwable) {
            // TODO Log event running failure
        }
    }
}