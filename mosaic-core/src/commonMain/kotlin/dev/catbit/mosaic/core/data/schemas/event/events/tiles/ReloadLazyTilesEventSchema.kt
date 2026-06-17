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
 * Sends a reload signal to a specific lazy tile (e.g., LazyColumn, LazyRow) identified by
 * [lazyTileId], causing it to discard its current content and re-fetch or re-render tiles
 * from scratch. The signal is dispatched via [tilesEventDispatcher] using the
 * [LazyTilesTileEvents.OnReloadTiles] event.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the reload signal is dispatched successfully.
 * - [OnFailureEventTrigger] — if the target lazy tile is not found (TileNotFoundException);
 *   incomingData is the exception.
 *
 * **Failure scenarios:**
 * - If [lazyTileId] does not match any tile currently registered with the dispatcher, a
 *   TileNotFoundException is thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:**
 * - This event operates through the internal tile event bus, not through the overlay or
 *   broadcast system used by other events. The target tile must be rendered and active for
 *   the reload to take effect.
 * - This is the correct way to programmatically reset a lazy list after a data change; it
 *   is preferable to [WipeTilesEventSchema] for lazy tiles because it allows the tile to
 *   handle its own reload lifecycle (pagination reset, scroll position, etc.).
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("ReloadLazyTiles")
data class ReloadLazyTilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("lazyTileId") val lazyTileId: String
) : EventSchema
