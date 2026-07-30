package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_time_loop

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartTimeLoopEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object StartTimeLoopEventRunner : EventRunner<StartTimeLoopEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: StartTimeLoopEventSchema) {
        with(event) {
            CoroutineScope(currentCoroutineContext()).launch {
                while (true) {
                    when (val time = timeData) {
                        is StartTimeLoopEventSchema.TimeData.Milliseconds -> {
                            delay(time.delay.milliseconds)
                        }

                        is StartTimeLoopEventSchema.TimeData.Seconds -> {
                            delay(time.delay.seconds)
                        }
                    }
                    onTrigger(EventTriggers.onTimeLoop())
                }
            }
        }
    }
}
