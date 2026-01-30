package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

class AddTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val groupingTileId: String,
    private val position: dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema.InsertionPosition,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val tiles: TileSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema> {

    override fun build(): dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema {
        return dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema(
            id = id,
            trigger = trigger,
            events = EventSchemaBuilderScope().apply(events).build(),
            groupingTileId = groupingTileId,
            tiles = TileSchemaBuilderScope().apply(tiles).build(),
            position = position
        )
    }
}

fun EventSchemaBuilderScope.AddTiles(
    id: String = randomUuid(),
    trigger: EventTrigger,
    groupingTileId: String,
    position: dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema.InsertionPosition,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tiles: TileSchemaBuilderScope.() -> Unit = {},
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