package dev.catbit.mosaic.server.builder.event.builders.system

import dev.catbit.mosaic.core.data.schemas.event.events.system.DropCachesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DropCachesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val dropScreensCache: Boolean,
    private val dropInitialGraphCache: Boolean,
    private val dropVersionCache: Boolean,
) : EventSchemaBuilder<DropCachesEventSchema>() {

    override fun build() = DropCachesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        dropScreensCache = dropScreensCache,
        dropInitialGraphCache = dropInitialGraphCache,
        dropVersionCache = dropVersionCache,
    )
}

/**
 * Clears the client's local caches: [dropScreensCache] the cached screen payloads,
 * [dropInitialGraphCache] the cached initial navigation graph, and [dropVersionCache] the cached
 * version marker used to decide whether cached content is still valid. Does not consume
 * `incomingData`. Dispatches `onSuccess` (no data) when the selected caches were dropped;
 * `onFailure` (carrying the thrown exception, logged) when dropping fails.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param dropScreensCache Whether to clear cached screen payloads.
 * @param dropInitialGraphCache Whether to clear the cached initial navigation graph.
 * @param dropVersionCache Whether to clear the cached version marker.
 */
fun EventSchemaBuilderScope.DropCaches(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    dropScreensCache: Boolean,
    dropInitialGraphCache: Boolean,
    dropVersionCache: Boolean,
) {
    addBuilder(
        DropCachesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            dropScreensCache = dropScreensCache,
            dropInitialGraphCache = dropInitialGraphCache,
            dropVersionCache = dropVersionCache,
        )
    )
}
