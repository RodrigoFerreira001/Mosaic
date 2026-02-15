package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.evaluate_data

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema

object EvaluateDataEventRunner : EventRunner<EvaluateDataEventSchema> {

    override fun EventRunningScope.runEvent(event: EvaluateDataEventSchema) {
        runSuspendOnScreenScope {

        }
        println("EvaluateDataEventRunner.runEvent: $event")
    }
}
