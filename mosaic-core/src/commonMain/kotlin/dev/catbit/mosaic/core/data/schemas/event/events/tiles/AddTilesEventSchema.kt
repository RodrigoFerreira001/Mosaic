package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Inserts one or more new tiles into a grouping tile's child list at the specified
 * [InsertionPosition]. The tiles are built from the server-supplied [TileSchema] list and
 * added to the live tile tree without requiring a full screen reload.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the tiles are successfully added to the grouping tile.
 * - [OnFailureEventTrigger] — if the target grouping tile is not found (TileNotFoundException);
 *   incomingData is the exception.
 *
 * **Failure scenarios:**
 * - If [groupingTileId] does not match any tile in the current tree, a TileNotFoundException is
 *   thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:**
 * - [groupingTileId] must reference an existing container tile (e.g., Column, Row, LazyColumn)
 *   that is currently in the tile tree; targeting an unknown ID triggers [OnFailureEventTrigger].
 * - [InsertionPosition] offers five variants: [InsertionPosition.Start], [InsertionPosition.End],
 *   [InsertionPosition.BeforeTile], [InsertionPosition.AfterTile], and [InsertionPosition.AtIndex].
 *   Using [BeforeTile] or [AfterTile] with an ID that does not exist inside the grouping tile
 *   may result in a no-op or an append depending on the tiles editor implementation.
 * - A TODO in the runner notes that ID generation (e.g., `[GENERATE#1]` placeholders) is not
 *   yet implemented; tile IDs must be unique and fully specified by the server.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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
