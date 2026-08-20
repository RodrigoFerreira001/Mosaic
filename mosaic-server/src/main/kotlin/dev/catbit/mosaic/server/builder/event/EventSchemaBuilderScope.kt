package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.server.builder.GenericBuilderScope

/**
 * The DSL receiver behind every `events = { ... }` block — what a tile's own `events` parameter and
 * an event's own nested `events` parameter (its outgoing-trigger children) both run against, as well
 * as [EventList]'s standalone block. Each nested event call inside the block builds its own
 * `EventSchemaBuilder` and registers it via
 * [addBuilder][dev.catbit.mosaic.server.builder.GenericBuilderScope.addBuilder]; the enclosing call
 * collects the results via [build][dev.catbit.mosaic.server.builder.GenericBuilderScope.build].
 */
class EventSchemaBuilderScope : GenericBuilderScope<EventSchema, EventSchemaBuilder<*>>()
