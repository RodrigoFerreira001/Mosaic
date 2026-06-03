package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.transform_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.TransformDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object TransformDataEventRunner : EventRunner<TransformDataEventSchema> {

    override fun EventRunningScope.runEvent(event: TransformDataEventSchema) {
        val result = runCatching { TemplateProcessor.applyTemplate(event.template, incomingData) }
            .getOrElse {
                onTrigger(EventTriggers.onFailure(), data = it)
                logError(
                    tag = "TransformDataEventRunner",
                    throwable = it
                )
                return
            }
        onTrigger(EventTriggers.onSuccess(), data = result)
    }
}
