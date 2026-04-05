package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
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

fun EventSchemaBuilderScope.ReplaceTiles(
    id: String = randomUuid(),
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
