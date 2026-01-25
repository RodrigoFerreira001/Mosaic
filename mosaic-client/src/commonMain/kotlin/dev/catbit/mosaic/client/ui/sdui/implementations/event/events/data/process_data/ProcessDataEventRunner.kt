package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.event.events.data.ProcessDataEventModel

object ProcessDataEventRunner : EventRunner<ProcessDataEventModel> {
    override suspend fun EventRunningScope.runEvent(event: ProcessDataEventModel) {
        println("executed ProcessDataEventModel")
    }
}
