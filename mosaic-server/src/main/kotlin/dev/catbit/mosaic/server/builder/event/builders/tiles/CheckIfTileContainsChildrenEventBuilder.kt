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
