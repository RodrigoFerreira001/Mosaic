package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.event.events.tiles.AddTilesEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventModelBuilder
import dev.catbit.mosaic.server.builder.event.EventModelBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileModelBuilderScope

class AddTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val groupingTileId: String,
    private val position: AddTilesEventModel.InsertionPosition,
    private val events: EventModelBuilderScope.() -> Unit = {},
    private val tiles: TileModelBuilderScope.() -> Unit = {},
) : EventModelBuilder<AddTilesEventModel> {

    override fun build(): AddTilesEventModel {
        return AddTilesEventModel(
            id = id,
            trigger = trigger,
            events = EventModelBuilderScope().apply(events).build(),
            groupingTileId = groupingTileId,
            tiles = TileModelBuilderScope().apply(tiles).build(),
            position = position
        )
    }
}

fun EventModelBuilderScope.AddTiles(
    id: String = randomUuid(),
    trigger: EventTrigger,
    groupingTileId: String,
    position: AddTilesEventModel.InsertionPosition,
    events: EventModelBuilderScope.() -> Unit = {},
    tiles: TileModelBuilderScope.() -> Unit = {},
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