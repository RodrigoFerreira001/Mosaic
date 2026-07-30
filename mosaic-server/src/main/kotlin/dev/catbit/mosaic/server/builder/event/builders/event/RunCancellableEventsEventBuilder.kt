package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.RunCancellableEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class RunCancellableEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val cancellableEventId: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<RunCancellableEventsEventSchema>() {

    override fun build() = RunCancellableEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        cancellableEventId = cancellableEventId,
    )
}

fun EventSchemaBuilderScope.RunCancellableEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    cancellableEventId: String,
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        RunCancellableEventsEventBuilder(
            id = id,
            trigger = trigger,
            cancellableEventId = cancellableEventId,
            events = events,
        )
    )
}
