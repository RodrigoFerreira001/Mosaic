package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.server.builder.GenericBuilder

class EventListBuilder(
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<List<EventSchema>> {

    override fun build() = EventSchemaBuilderScope().apply(events).build()
}

fun EventList(
    events: EventSchemaBuilderScope.() -> Unit = {},
) = EventListBuilder(
    events = events,
).build()