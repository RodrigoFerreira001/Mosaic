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

fun milliseconds(delay: Long): TimeData.Milliseconds {
    require(delay > 0) { "delay must be a positive value, was $delay" }
    return TimeData.Milliseconds(delay = delay)
}

fun seconds(delay: Int): TimeData.Seconds {
    require(delay > 0) { "delay must be a positive value, was $delay" }
    return TimeData.Seconds(delay = delay)
}
