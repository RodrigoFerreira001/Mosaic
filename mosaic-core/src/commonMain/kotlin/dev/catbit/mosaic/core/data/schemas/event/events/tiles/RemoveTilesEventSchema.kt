package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Removes specific tiles from a grouping tile's child list by their IDs. The tiles are
 * deleted from the live tile tree without requiring a full screen reload.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the tiles are successfully removed from the grouping tile.
 * - [OnFailureEventTrigger] — if the target grouping tile is not found (TileNotFoundException);
 *   incomingData is the exception.
 *
 * **Failure scenarios:**
 * - If [groupingTileId] does not match any tile in the current tree, a TileNotFoundException is
 *   thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:**
 * - [groupingTileId] must reference an existing container tile currently in the tile tree.
 * - IDs in [tileIds] that do not exist within the grouping tile are silently ignored.
 * - Removing all tiles from a container is equivalent to [WipeTilesEventSchema] when all
 *   IDs are specified, but [WipeTilesEventSchema] is more efficient for clearing the entire
 *   list because it does not require enumerating individual tile IDs.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("RemoveTiles")
data class RemoveTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("groupingTileId") val groupingTileId: String,
    @SerialName("tileIds") val tileIds: SerializableImmutableList<String>
) : EventSchema
