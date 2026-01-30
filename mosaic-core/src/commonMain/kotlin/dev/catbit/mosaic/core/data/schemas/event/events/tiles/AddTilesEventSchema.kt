package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesAddedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTilesAddedEventTrigger::class
    ]
)
@Serializable
@SerialName("AddTiles")
data class AddTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val groupingTileId: String,
    val tiles: List<TileSchema>,
    val position: InsertionPosition
) : EventSchema {

    @Serializable
    sealed interface InsertionPosition {
        @Serializable
        @SerialName("Start")
        data object Start : InsertionPosition

        @Serializable
        @SerialName("End")
        data object End : InsertionPosition

        @Serializable
        @SerialName("BeforeTile")
        data class BeforeTile(val tileId: String) : InsertionPosition

        @Serializable
        @SerialName("AfterTile")
        data class AfterTile(val tileId: String) : InsertionPosition

        @Serializable
        @SerialName("AtIndex")
        data class AtIndex(val index: Int) : InsertionPosition
    }
}
