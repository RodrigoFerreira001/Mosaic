package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.data.UpdateDataEventModel

object UpdateDataEventRunner : EventRunner<UpdateDataEventModel> {
    override suspend fun EventRunningScope.runEvent(event: UpdateDataEventModel) {
        println("executed UpdateDataEventModel")
    }
}
