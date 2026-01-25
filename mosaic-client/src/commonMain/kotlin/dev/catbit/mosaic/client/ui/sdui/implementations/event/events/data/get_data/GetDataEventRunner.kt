package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.data.GetDataEventModel

object GetDataEventRunner : EventRunner<GetDataEventModel> {
    override suspend fun EventRunningScope.runEvent(event: GetDataEventModel) {
        println("executed GetDataEventModel")
    }
}
