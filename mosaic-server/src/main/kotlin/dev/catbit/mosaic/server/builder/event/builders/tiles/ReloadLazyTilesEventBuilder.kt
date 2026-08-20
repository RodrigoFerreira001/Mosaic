package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReloadLazyTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ReloadLazyTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val lazyTileId: String
) : EventSchemaBuilder<ReloadLazyTilesEventSchema>() {

    override fun build() = ReloadLazyTilesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        lazyTileId = lazyTileId
    )
}

/**
 * Resets the `LazyTiles` tile identified by [lazyTileId] back to its loading state — it drops
 * the tiles it had loaded, clears its failure flag, and fires its request again, the way to
 * retry after a failed load. Does not consume `incomingData`. Dispatches `onSuccess` (no data)
 * when the signal reached the tile — note this reports the reset, not the reload that follows;
 * the load's own outcome arrives through the tile's own load-success/load-failure triggers.
 * `onFailure` (carrying the thrown exception) fires when no tile with [lazyTileId] is currently
 * mounted.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param lazyTileId Id of the `LazyTiles` tile to reset and reload.
 */
fun EventSchemaBuilderScope.ReloadLazyTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    lazyTileId: String
) {
    addBuilder(
        ReloadLazyTilesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            lazyTileId = lazyTileId
        )
    )
}
