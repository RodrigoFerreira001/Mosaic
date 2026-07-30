package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.CancelEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class CancelEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val cancellableEventId: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<CancelEventsEventSchema>() {

    override fun build() = CancelEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        cancellableEventId = cancellableEventId,
    )
}

fun EventSchemaBuilderScope.CancelEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    cancellableEventId: String,
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        CancelEventsEventBuilder(
            id = id,
            trigger = trigger,
            cancellableEventId = cancellableEventId,
            events = events,
        )
    )
}
