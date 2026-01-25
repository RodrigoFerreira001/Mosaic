package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.data.RemoveDataEventModel

object RemoveDataEventRunner : EventRunner<RemoveDataEventModel> {
    override suspend fun EventRunningScope.runEvent(event: RemoveDataEventModel) {
        println("executed RemoveDataEventModel")
    }
}
