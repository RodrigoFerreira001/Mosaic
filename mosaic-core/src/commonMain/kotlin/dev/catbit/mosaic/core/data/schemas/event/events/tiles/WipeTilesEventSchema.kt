package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Removes all children from a grouping tile in a single operation, leaving the container
 * empty. This is more efficient than [RemoveTilesEventSchema] when the goal is to clear
 * the entire list, because no individual tile IDs need to be enumerated.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the wipe is completed successfully.
 * - [OnFailureEventTrigger] — if the target grouping tile is not found (TileNotFoundException);
 *   incomingData is the exception.
 *
 * **Failure scenarios:**
 * - If [groupingTileId] does not match any tile in the current tree, a TileNotFoundException is
 *   thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:**
 * - [groupingTileId] must reference an existing container tile currently in the tile tree.
 * - Wiping an already-empty container is also a no-op.
 * - After a wipe, [AddTilesEventSchema] or [ReplaceTilesEventSchema] can be used to
 *   repopulate the container.
 */
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("WipeTiles")
data class WipeTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val groupingTileId: String
) : EventSchema
