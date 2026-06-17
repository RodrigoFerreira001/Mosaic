package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data

import dev.catbit.mosaic.client.ui.sdui.foundation.data_mailer.DataMailer
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object SendDataEventRunner : EventRunner<SendDataEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: SendDataEventSchema) {
        with(event) {
            (data ?: incomingData)?.let { validData ->
                get<DataMailer>().sendData(
                    data = validData,
                    dataKey = dataKey
                )
                onTrigger(EventTriggers.onSuccess())
            } ?: run {
                onTrigger(EventTriggers.onFailure())
                logError(
                    tag = "SendDataEventRunner",
                    throwable = Throwable("No data to send")
                )
            }
        }
    }
}
