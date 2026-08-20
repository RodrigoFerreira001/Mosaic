package dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh

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
 * Stops the loading indicator of the `PullToRefresh` tile identified by [tileId], by dispatching
 * a `PullToRefreshTileEvents.StopRefreshing` to it. The tile never hides the spinner on its own,
 * so this event has to close every refresh flow — on the success branch and on the failure branch
 * alike.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the signal reached the tile. No data is passed downstream.
 * - `OnFailureEventTrigger` — when no tile with [tileId] is currently mounted; the `Throwable` is
 *   passed as incomingData.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("StopRefreshing")
data class StopRefreshingEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("tileId") val tileId: String
) : EventSchema