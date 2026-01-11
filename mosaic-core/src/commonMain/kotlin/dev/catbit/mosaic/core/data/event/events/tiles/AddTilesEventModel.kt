package dev.catbit.mosaic.core.data.event.events.tiles

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("AddTiles")
data class AddTilesEventModel(
    val groupingTileId: String,
    val tiles: List<TileModel>,
    val position: InsertionPosition,
    override val id: String,
    override val trigger: EventTrigger,
    override val events: List<EventModel>?
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
