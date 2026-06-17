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
 * Replaces the entire child list of a grouping tile with a new set of server-supplied tiles.
 * This is effectively an atomic wipe-then-add: the previous children are discarded and the
 * new [tiles] list becomes the grouping tile's complete content.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the replacement is completed successfully.
 * - [OnFailureEventTrigger] — if the target grouping tile is not found (TileNotFoundException);
 *   incomingData is the exception.
 *
 * **Failure scenarios:**
 * - If [groupingTileId] does not match any tile in the current tree, a TileNotFoundException is
 *   thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:**
 * - [groupingTileId] must reference an existing container tile currently in the tile tree.
 * - Unlike [AddTilesEventSchema], there is no [InsertionPosition] — the new tiles always
 *   fully replace the existing list rather than being merged into it.
 * - Sending an empty [tiles] list produces the same result as [WipeTilesEventSchema] for
 *   the target grouping tile.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("ReplaceTiles")
data class ReplaceTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("groupingTileId") val groupingTileId: String,
    @SerialName("tileIds") val tiles: SerializableImmutableList<TileSchema>,
) : EventSchema
