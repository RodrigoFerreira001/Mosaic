package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Appends [tiles] as children of the grouping tile identified by [groupingTileId], without
 * rebuilding the rest of the screen.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the children were added. No data is passed downstream.
 * - `OnFailureEventTrigger` — when no grouping tile carries [groupingTileId], or it cannot hold
 *   children; the `Throwable` is passed as incomingData and the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("AddTiles")
data class AddTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    val groupingTileId: String,
    val tiles: SerializableImmutableList<TileSchema>,
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
