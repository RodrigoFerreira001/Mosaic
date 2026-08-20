package dev.catbit.mosaic.server.builder.event.builders.time

import dev.catbit.mosaic.core.data.schemas.event.events.time.StartTimeLoopEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartTimeLoopEventSchema.TimeData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class StartTimeLoopEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val timeData: TimeData
) : EventSchemaBuilder<StartTimeLoopEventSchema>() {

    override fun build() = StartTimeLoopEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        timeData = timeData
    )
}

/**
 * Starts an endless loop in a coroutine launched from the running event's context, firing once
 * per period set by [timeData] — built with [milliseconds] or [seconds]. The first fire happens
 * after the first delay, not immediately. The event returns as soon as the loop is launched, so
 * the chain continues while it keeps running; the loop runs forever by design, so to be able to
 * stop it, launch it from inside a `RunCancellableEvents` and cancel that id with `CancelEvents`.
 * Does not consume `incomingData`. Dispatches `onTimeLoop` once per period, indefinitely — no
 * data is passed, and neither success nor failure is reported.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its trigger (`onTimeLoop`).
 * @param timeData Period and unit between fires, built with [milliseconds] or [seconds].
 */
fun EventSchemaBuilderScope.StartTimeLoop(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    timeData: TimeData
) {
    addBuilder(
        StartTimeLoopEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            timeData = timeData
        )
    )
}

/** Loop period expressed in milliseconds, for `StartTimeLoop`. Must be positive. */
fun milliseconds(delay: Long): TimeData.Milliseconds {
    require(delay > 0) { "delay must be a positive value, was $delay" }
    return TimeData.Milliseconds(delay = delay)
}

/** Loop period expressed in seconds, for `StartTimeLoop`. Must be positive. */
fun seconds(delay: Int): TimeData.Seconds {
    require(delay > 0) { "delay must be a positive value, was $delay" }
    return TimeData.Seconds(delay = delay)
}
