package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data

import dev.catbit.mosaic.client.ui.sdui.foundation.data_mailer.DataMailer
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object CheckForReceivedDataEventRunner : EventRunner<CheckForReceivedDataEventSchema> {
    override fun EventRunningScope.runEvent(event: CheckForReceivedDataEventSchema) {
        get<DataMailer>().getData(event.dataKey)?.let { validData ->
            onTrigger(
                eventTrigger = EventTriggers.onDataReceived(),
                data = validData
            )
        }
    }
}
