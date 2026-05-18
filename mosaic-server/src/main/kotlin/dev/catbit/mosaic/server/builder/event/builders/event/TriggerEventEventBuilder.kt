package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class TriggerEventEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val eventId: String
) : EventSchemaBuilder<TriggerEventEventSchema>() {

    override fun build() = TriggerEventEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        eventId = eventId
    )
}

fun EventSchemaBuilderScope.TriggerEvent(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    eventId: String
) {
    addBuilder(
        TriggerEventEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            eventId = eventId
        )
    )
}
