package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList

internal class RemoveTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val groupingTileId: String,
    private val tileIds: List<String>
) : EventSchemaBuilder<RemoveTilesEventSchema>() {

    override fun build() = RemoveTilesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        groupingTileId = groupingTileId,
        tileIds = tileIds.toImmutableList()
    )
}

/**
 * Removes the children listed in [tileIds] from the grouping tile identified by
 * [groupingTileId] — ids that aren't among its children are ignored. Does not consume
 * `incomingData`. Dispatches `onSuccess` (no data) when the removal completed; `onFailure`
 * (carrying the thrown exception, logged) when no grouping tile carries [groupingTileId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param groupingTileId Id of the grouping tile to remove children from.
 * @param tileIds Ids of the children to remove.
 */
fun EventSchemaBuilderScope.RemoveTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    groupingTileId: String,
    tileIds: List<String>
) {
    addBuilder(
        RemoveTilesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            groupingTileId = groupingTileId,
            tileIds = tileIds
        )
    )
}
