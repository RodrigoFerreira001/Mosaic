package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema

object SendDataEventRunner : EventRunner<SendDataEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: SendDataEventSchema) {
        println("executed SendDataEventModel")
    }
}
