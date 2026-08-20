package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class ReplaceTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val groupingTileId: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit
) : EventSchemaBuilder<ReplaceTilesEventSchema>() {

    override fun build() = ReplaceTilesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        groupingTileId = groupingTileId,
        tiles = TileSchemaBuilderScope().apply(tiles).build()
    )
}

/**
 * Swaps the whole children list of the grouping tile identified by [groupingTileId] for [tiles].
 * Does not consume `incomingData`. Dispatches `onSuccess` (no data) when the children were
 * replaced; `onFailure` (carrying the thrown exception, logged) when no grouping tile carries
 * [groupingTileId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param groupingTileId Id of the grouping tile whose children are replaced.
 * @param tiles New tiles that replace the whole children list.
 */
fun EventSchemaBuilderScope.ReplaceTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    groupingTileId: String,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        ReplaceTilesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            groupingTileId = groupingTileId,
            tiles = tiles
        )
    )
}
