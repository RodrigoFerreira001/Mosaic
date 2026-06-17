package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.RunEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class RunEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<RunEventsEventSchema>() {

    override fun build() = RunEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
    )
}

fun EventSchemaBuilderScope.RunEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        RunEventsEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
        )
    )
}
