package dev.catbit.mosaic.server.builder.event.builders.time

import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema.TimerData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class StartCountdownTimerEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val timerData: TimerData
) : EventSchemaBuilder<StartCountdownTimerEventSchema>() {

    override fun build() = StartCountdownTimerEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        timerData = timerData
    )
}

fun EventSchemaBuilderScope.StartCountdownTimer(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    timerData: TimerData
) {
    addBuilder(
        StartCountdownTimerEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            timerData = timerData
        )
    )
}

fun milliseconds(initial: Long, step: Long): TimerData.Milliseconds {
    require(initial > 0) { "initial must be a positive value, was $initial" }
    require(step >= 0) { "step must not be negative, was $step" }
    require(step < initial) { "step ($step) must be less than initial ($initial)" }
    return TimerData.Milliseconds(initial = initial, step = step)
}

fun seconds(initial: Int, step: Int): TimerData.Seconds {
    require(initial > 0) { "initial must be a positive value, was $initial" }
    require(step >= 0) { "step must not be negative, was $step" }
    require(step < initial) { "step ($step) must be less than initial ($initial)" }
    return TimerData.Seconds(initial = initial, step = step)
}
