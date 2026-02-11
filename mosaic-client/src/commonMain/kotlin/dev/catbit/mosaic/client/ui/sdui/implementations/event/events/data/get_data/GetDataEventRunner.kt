package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema

object GetDataEventRunner : EventRunner<GetDataEventSchema> {
    override fun EventRunningScope.runEvent(event: GetDataEventSchema) {
        println("executed GetDataEventModel")
    }
}
