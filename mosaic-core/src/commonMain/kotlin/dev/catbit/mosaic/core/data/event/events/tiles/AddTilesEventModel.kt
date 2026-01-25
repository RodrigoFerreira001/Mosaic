package dev.catbit.mosaic.core.data.event.events.tiles

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTilesAddedEventTrigger
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTilesAddedEventTrigger::class
    ]
)
@Serializable
@SerialName("AddTiles")
data class AddTilesEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    val groupingTileId: String,
    val tiles: List<TileModel>,
    val position: InsertionPosition
) : EventModel {

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
