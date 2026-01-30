package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema

object CheckForReceivedDataEventRunner : EventRunner<CheckForReceivedDataEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: CheckForReceivedDataEventSchema) {
        println("executed CheckForReceivedDataEventModel")
    }
}
