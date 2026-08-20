package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.GetTileChildrenCountEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class GetTileChildrenCountEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val groupingTileId: String
) : EventSchemaBuilder<GetTileChildrenCountEventSchema>() {

    override fun build() = GetTileChildrenCountEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        groupingTileId = groupingTileId
    )
}

/**
 * Reads how many children the grouping tile identified by [groupingTileId] currently holds. Does
 * not consume `incomingData`. Dispatches `onSuccess` (carrying the count as an `Int`); `onFailure`
 * (no data) when no grouping tile carries [groupingTileId], or it cannot hold children.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param groupingTileId Id of the grouping tile whose child count is read.
 */
fun EventSchemaBuilderScope.GetTileChildrenCount(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    groupingTileId: String
) {
    addBuilder(
        GetTileChildrenCountEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            groupingTileId = groupingTileId
        )
    )
}
