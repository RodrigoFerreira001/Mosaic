package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.event.EventModel

interface EventRunner<out T : EventModel> {
    fun canRun(event: EventModel): Boolean
    fun EventRunningScope.runEvent(event: @UnsafeVariance T)
}