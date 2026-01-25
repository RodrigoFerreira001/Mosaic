package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.data.SendDataEventModel

object SendDataEventRunner : EventRunner<SendDataEventModel> {
    override suspend fun EventRunningScope.runEvent(event: SendDataEventModel) {
        println("executed SendDataEventModel")
    }
}
