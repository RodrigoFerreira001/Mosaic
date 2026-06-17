package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder

class EventListBuilder(
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<SerializableImmutableList<EventSchema>>() {

    override fun build(): SerializableImmutableList<EventSchema> =
        EventSchemaBuilderScope().apply(events).build()
}

fun EventList(
    events: EventSchemaBuilderScope.() -> Unit = {},
): SerializableImmutableList<EventSchema> = EventListBuilder(
    events = events,
).build()