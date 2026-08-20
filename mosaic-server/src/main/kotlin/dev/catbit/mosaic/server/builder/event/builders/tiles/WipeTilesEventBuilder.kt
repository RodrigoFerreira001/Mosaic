package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.WipeTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class WipeTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val groupingTileId: String
) : EventSchemaBuilder<WipeTilesEventSchema>() {

    override fun build() = WipeTilesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        groupingTileId = groupingTileId
    )
}

/**
 * Removes every child of the grouping tile identified by [groupingTileId], leaving it empty.
 * Does not consume `incomingData`. Dispatches `onSuccess` (no data) when the children were
 * removed; `onFailure` (carrying the thrown exception, logged) when no grouping tile carries
 * [groupingTileId].
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param groupingTileId Id of the grouping tile to empty.
 */
fun EventSchemaBuilderScope.WipeTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    groupingTileId: String
) {
    addBuilder(
        WipeTilesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            groupingTileId = groupingTileId
        )
    )
}
