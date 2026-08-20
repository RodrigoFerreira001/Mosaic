package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder

/**
 * Wraps an [EventSchemaBuilderScope] block into a plain [GenericBuilder] producing the built event
 * list directly, rather than the schema a normal tile/event builder produces — the mechanism behind
 * the top-level [EventList] function. Not used anywhere in the framework's own built-in tiles/events
 * or samples at the time of writing; exists as a public DSL entry point for a caller that wants a
 * standalone `List<EventSchema>` (e.g. to assemble outside any single tile/event's own `events =`
 * parameter) without hand-building a [dev.catbit.mosaic.core.data.schemas.event.EventSchema] list.
 *
 * @param events the event block to build.
 */
class EventListBuilder(
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<SerializableImmutableList<EventSchema>>() {

    override fun build(): SerializableImmutableList<EventSchema> =
        EventSchemaBuilderScope().apply(events).build()
}

/**
 * Builds [events] into a standalone `List<EventSchema>` — a thin wrapper over [EventListBuilder],
 * for a DSL author who needs a list of events outside a single tile/event's own `events =`
 * parameter (e.g. to build a shared list once and reuse it in more than one place).
 *
 * @param events the event block to build.
 * @return the built events, in declaration order.
 */
fun EventList(
    events: EventSchemaBuilderScope.() -> Unit = {},
): SerializableImmutableList<EventSchema> = EventListBuilder(
    events = events,
).build()