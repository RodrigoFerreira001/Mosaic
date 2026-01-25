package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.event.TriggerEventEventModel

object TriggerEventEventRunner : EventRunner<TriggerEventEventModel> {
    override suspend fun EventRunningScope.runEvent(event: TriggerEventEventModel) {
        println("executed TriggerEventEventModel")
    }
}
