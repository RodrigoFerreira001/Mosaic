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

/**
 * Starts a countdown in a coroutine launched from the running event's context, ticking down from
 * [timerData]'s `initial` toward zero in `step`-sized decrements, waiting one step between ticks
 * — built with [milliseconds] or [seconds]. The event returns as soon as the countdown is
 * launched, so the chain continues while the timer runs in the background; it stops on its own
 * when the countdown ends. To stop it early, launch it from inside a `RunCancellableEvents` and
 * cancel that id with `CancelEvents`. Does not consume `incomingData`. Dispatches
 * `onCountdownTimerTick` once per tick (carrying the remaining amount), and `onTimeFinish` once
 * after the last tick — neither success nor failure is reported.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onCountdownTimerTick`, `onTimeFinish`).
 * @param timerData Starting value, step size and unit, built with [milliseconds] or [seconds].
 */
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

/**
 * Countdown expressed in milliseconds, for `StartCountdownTimer`.
 *
 * @param initial Starting value, in milliseconds. Must be positive.
 * @param step Amount subtracted per tick, in milliseconds. Must be non-negative and smaller than [initial].
 */
fun milliseconds(initial: Long, step: Long): TimerData.Milliseconds {
    require(initial > 0) { "initial must be a positive value, was $initial" }
    require(step >= 0) { "step must not be negative, was $step" }
    require(step < initial) { "step ($step) must be less than initial ($initial)" }
    return TimerData.Milliseconds(initial = initial, step = step)
}

/**
 * Countdown expressed in seconds, for `StartCountdownTimer`.
 *
 * @param initial Starting value, in seconds. Must be positive.
 * @param step Amount subtracted per tick, in seconds. Must be non-negative and smaller than [initial].
 */
fun seconds(initial: Int, step: Int): TimerData.Seconds {
    require(initial > 0) { "initial must be a positive value, was $initial" }
    require(step >= 0) { "step must not be negative, was $step" }
    require(step < initial) { "step ($step) must be less than initial ($initial)" }
    return TimerData.Seconds(initial = initial, step = step)
}
