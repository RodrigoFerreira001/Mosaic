package dev.catbit.mosaic.server.builder.event.builders.pull_to_refresh

import dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh.StopRefreshingEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class StopRefreshingEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tileId: String
) : EventSchemaBuilder<StopRefreshingEventSchema>() {

    override fun build() = StopRefreshingEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        tileId = tileId
    )
}

/**
 * Stops the loading indicator of the `PullToRefresh` tile identified by [tileId] — the tile
 * never hides its spinner on its own, so this event must close every refresh flow, on both the
 * success and failure branches. Does not consume `incomingData`. Dispatches `onSuccess` (no data)
 * when the signal reached the tile; `onFailure` (carrying the thrown exception) when no tile with
 * [tileId] is currently mounted.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param tileId Id of the `PullToRefresh` tile to stop refreshing.
 */
fun EventSchemaBuilderScope.StopRefreshing(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tileId: String
) {
    addBuilder(
        StopRefreshingEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            tileId = tileId
        )
    )
}
