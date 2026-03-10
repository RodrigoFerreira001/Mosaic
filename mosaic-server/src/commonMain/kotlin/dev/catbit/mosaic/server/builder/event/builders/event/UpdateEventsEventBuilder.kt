package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class UpdateEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val updates: List<UpdateEventsEventSchema.Update>
) : EventSchemaBuilder<UpdateEventsEventSchema> {

    override fun build() = UpdateEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        updates = updates
    )
}

fun EventSchemaBuilderScope.UpdateEvents(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    updates: List<UpdateEventsEventSchema.Update>
) {
    addBuilder(
        UpdateEventsEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            updates = updates
        )
    )
}
