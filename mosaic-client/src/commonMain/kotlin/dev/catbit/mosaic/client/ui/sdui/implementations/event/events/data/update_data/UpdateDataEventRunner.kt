package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema

object UpdateDataEventRunner : EventRunner<UpdateDataEventSchema> {
    override fun EventRunningScope.runEvent(event: UpdateDataEventSchema) {
        println("executed UpdateDataEventModel")
    }
}
