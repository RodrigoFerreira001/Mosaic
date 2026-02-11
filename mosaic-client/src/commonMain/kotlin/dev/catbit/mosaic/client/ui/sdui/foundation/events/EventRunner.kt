package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.schemas.event.EventSchema

interface EventRunner<out T : EventSchema> {
    fun EventRunningScope.runEvent(event: @UnsafeVariance T)
}