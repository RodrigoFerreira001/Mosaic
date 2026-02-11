package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema

object ProcessDataEventRunner : EventRunner<ProcessDataEventSchema> {
    override fun EventRunningScope.runEvent(event: ProcessDataEventSchema) {
        println("executed ProcessDataEventModel")
    }
}
