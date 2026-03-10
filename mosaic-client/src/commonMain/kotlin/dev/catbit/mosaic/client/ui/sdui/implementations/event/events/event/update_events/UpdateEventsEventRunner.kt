package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.update_events

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema

object UpdateEventsEventRunner : EventRunner<UpdateEventsEventSchema> {
    override fun EventRunningScope.runEvent(event: UpdateEventsEventSchema) {
        println("executed UpdateEventsEvent")
    }
}
