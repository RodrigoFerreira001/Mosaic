package dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Signals a PullToRefreshTile to stop its loading indicator and return to the idle state.
 * At runtime the runner calls [tilesEventDispatcher.onEvent] with [PullToRefreshTileEvents.StopRefreshing]
 * targeting the tile identified by [tileId].
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the stop-refreshing signal is dispatched successfully.
 * - [OnFailureEventTrigger] — if no PullToRefreshTile with the given [tileId] is currently
 *   mounted (TileNotFoundException); incomingData is the exception.
 *
 * **Failure scenarios:** If no PullToRefreshTile with the given [tileId] is mounted, a
 * TileNotFoundException is thrown and [OnFailureEventTrigger] fires with the exception.
 *
 * **Notes:** This event is typically placed as a downstream event in a chain initiated by the
 * `OnPullToRefresh` trigger — e.g., after a network request completes (whether successfully or
 * with failure) the server includes this event to dismiss the spinner. Failing to fire it will
 * leave the refresh indicator spinning indefinitely.
 */
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("StopRefreshing")
data class StopRefreshingEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("tileId") val tileId: String
) : EventSchema