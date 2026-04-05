package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesReplacedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTilesReplacedEventTrigger::class
    ]
)
@Serializable
@SerialName("ReplaceTiles")
data class ReplaceTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("groupingTileId") val groupingTileId: String,
    @SerialName("tileIds") val tiles: List<TileSchema>,
) : EventSchema
