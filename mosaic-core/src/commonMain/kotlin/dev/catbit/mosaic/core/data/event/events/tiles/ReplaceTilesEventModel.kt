package dev.catbit.mosaic.core.data.event.events.tiles

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ReplaceTiles")
data class ReplaceTilesEventModel(
    val groupingTileId: String?,
    val tiles: List<TileModel>,
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventModel>?
) : EventModel
