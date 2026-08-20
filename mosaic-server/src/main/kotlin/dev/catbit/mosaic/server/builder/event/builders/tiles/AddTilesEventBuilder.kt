package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class AddTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val groupingTileId: String,
    private val position: AddTilesEventSchema.InsertionPosition,
    private val tiles: TileSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<AddTilesEventSchema>() {

    override fun build(): AddTilesEventSchema {
        return AddTilesEventSchema(
            id = id,
            trigger = trigger,
            events = EventSchemaBuilderScope().apply(events).build(),
            groupingTileId = groupingTileId,
            tiles = TileSchemaBuilderScope().apply(tiles).build(),
            position = position
        )
    }
}

/**
 * Appends [tiles] as children of the grouping tile identified by [groupingTileId], at [position],
 * without rebuilding the rest of the screen. Does not consume `incomingData`. Dispatches
 * `onSuccess` (no data) when the children were added; `onFailure` (carrying the thrown exception,
 * logged) when no grouping tile carries [groupingTileId], or it cannot hold children.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param groupingTileId Id of the grouping tile (`Column`, `Row`, `LazyColumn`, etc) to append children to.
 * @param position Where among the existing children to insert — [insertAtStart], [insertAtEnd], [insertBeforeTile], [insertAfterTile] or [insertAtIndex]. Defaults to the end.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param tiles Tiles appended as new children.
 */
fun EventSchemaBuilderScope.AddTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    groupingTileId: String,
    position: AddTilesEventSchema.InsertionPosition = insertAtEnd(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        AddTilesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            groupingTileId = groupingTileId,
            tiles = tiles,
            position = position
        )
    )
}

/** Inserts before every existing child. */
fun insertAtStart() = AddTilesEventSchema.InsertionPosition.Start

/** Inserts after every existing child. */
fun insertAtEnd() = AddTilesEventSchema.InsertionPosition.End

/** Inserts immediately before the child whose id is [tileId]. */
fun insertBeforeTile(tileId: String) = AddTilesEventSchema.InsertionPosition.BeforeTile(tileId)

/** Inserts immediately after the child whose id is [tileId]. */
fun insertAfterTile(tileId: String) = AddTilesEventSchema.InsertionPosition.AfterTile(tileId)

/** Inserts at the given zero-based [index] among the existing children. */
fun insertAtIndex(index: Int) = AddTilesEventSchema.InsertionPosition.AtIndex(index)