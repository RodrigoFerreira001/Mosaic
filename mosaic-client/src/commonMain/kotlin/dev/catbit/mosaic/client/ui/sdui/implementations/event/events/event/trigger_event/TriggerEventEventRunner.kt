package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema

object TriggerEventEventRunner : EventRunner<TriggerEventEventSchema> {
    override fun EventRunningScope.runEvent(event: TriggerEventEventSchema) {
        println("executed TriggerEventEventModel")
    }
}
