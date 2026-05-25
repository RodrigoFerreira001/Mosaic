package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

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
        tileIds = tileIds
    )
}

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
