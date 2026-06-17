package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.data_processor.DataProcessor
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object ProcessDataEventRunner : EventRunner<ProcessDataEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: ProcessDataEventSchema) {

        incomingData?.let {
            getAll<DataProcessor>()
                .firstOrNull { it.id == event.processWith }?.let { dataProcessor ->
                    with(dataProcessor) {
                        process(incomingData)
                    }
                        .onFailure {
                            onTrigger(EventTriggers.onFailure(), it)
                        }
                        .onSuccess {
                            onTrigger(EventTriggers.onSuccess())
                        }
                } ?: run {
                onTrigger(EventTriggers.onFailure())
            }
        } ?: run {
            onTrigger(EventTriggers.onFailure())
        }
    }
}
