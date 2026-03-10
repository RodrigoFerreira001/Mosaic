package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_countdown_timer

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema

object StartCountdownTimerEventRunner : EventRunner<StartCountdownTimerEventSchema> {
    override fun EventRunningScope.runEvent(event: StartCountdownTimerEventSchema) {
        println("executed StartCountdownTimerEvent")
    }
}
