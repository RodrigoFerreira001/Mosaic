package dev.catbit.mosaic.core.data.schemas.event.events.tiles

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Resets the `LazyTiles` tile identified by [lazyTileId] back to its loading state, by
 * dispatching a `LazyTilesTileEvents.OnReloadTiles` to it. The tile drops the tiles it had loaded
 * and clears its failure flag, which makes it fire its request again — the way to retry after a
 * failed load.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the signal reached the tile. Note this reports the reset, not
 *   the reload that follows: the load's own outcome arrives through the tile's
 *   `OnLoadTilesSuccess` / `OnLoadTilesFailure` triggers. No data is passed downstream.
 * - `OnFailureEventTrigger` — when no tile with [lazyTileId] is currently mounted; the `Throwable`
 *   is passed as incomingData.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
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
