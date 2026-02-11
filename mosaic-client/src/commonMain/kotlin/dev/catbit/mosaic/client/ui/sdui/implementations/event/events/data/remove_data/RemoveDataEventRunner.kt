package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema

object RemoveDataEventRunner : EventRunner<RemoveDataEventSchema> {
    override fun EventRunningScope.runEvent(event: RemoveDataEventSchema) {
        println("executed RemoveDataEventModel")
    }
}
