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
