package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_countdown_timer

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object StartCountdownTimerEventRunner : EventRunner<StartCountdownTimerEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: StartCountdownTimerEventSchema) {
        with(event) {
            CoroutineScope(currentCoroutineContext()).launch {
                when (val time = timerData) {
                    is StartCountdownTimerEventSchema.TimerData.Milliseconds -> {
                        for (tick in time.initial downTo time.step step time.step) {
                            onTrigger(
                                eventTrigger = EventTriggers.onTimeTick(),
                                data = tick
                            )
                            delay(time.step.milliseconds)
                        }
                    }
                    is StartCountdownTimerEventSchema.TimerData.Seconds -> {
                        for (tick in time.initial downTo time.step step time.step) {
                            onTrigger(
                                eventTrigger = EventTriggers.onTimeTick(),
                                data = tick
                            )
                            delay(time.step.seconds)
                        }
                    }
                }
                onTrigger(EventTriggers.onTimeFinish())
            }
        }
    }
}
