package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.CheckIfTileContainsChildrenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList

internal class CheckIfTileContainsChildrenEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val groupingTileId: String,
    private val childrenIds: List<String>
) : EventSchemaBuilder<CheckIfTileContainsChildrenEventSchema>() {

    override fun build() = CheckIfTileContainsChildrenEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        groupingTileId = groupingTileId,
        childrenIds = childrenIds.toImmutableList()
    )
}

/**
 * Tests whether the grouping tile identified by [groupingTileId] currently holds every child in
 * [childrenIds], and branches on the answer. Does not consume `incomingData`. Dispatches
 * `onSuccess` (no data) when all the listed children are present; `onFailure` (no data) when at
 * least one is missing, and also when no grouping tile carries [groupingTileId] — the two cases
 * are indistinguishable downstream.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param groupingTileId Id of the grouping tile whose children are checked.
 * @param childrenIds Ids that must all currently be children of [groupingTileId].
 */
fun EventSchemaBuilderScope.CheckIfTileContainsChildren(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    groupingTileId: String,
    childrenIds: List<String>
) {
    addBuilder(
        CheckIfTileContainsChildrenEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            groupingTileId = groupingTileId,
            childrenIds = childrenIds
        )
    )
}
