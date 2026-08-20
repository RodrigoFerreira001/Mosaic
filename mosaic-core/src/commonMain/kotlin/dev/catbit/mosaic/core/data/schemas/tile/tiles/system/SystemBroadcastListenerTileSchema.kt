package dev.catbit.mosaic.core.data.schemas.tile.tiles.system

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSystemBroadcastEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders [tiles] and, while they are on screen, subscribes to the app-wide system broadcast
 * channel. Every broadcast received is turned into a trigger, so this is the tile that lets
 * host-app signals (push notifications, connectivity changes, anything the client publishes on
 * that channel) drive server-declared event flows.
 *
 * **Triggers dispatched:**
 * - `OnSystemBroadcastEventTrigger` — fired for each broadcast, carrying the broadcast's own id
 *   so events can be wired per broadcast; the broadcast payload is passed as the event's
 *   incoming data.
 *
 * **Notes:** the subscription lives only as long as the tile is composed — while it is off
 * screen, broadcasts are not observed. The children are hosted in a `Box` carrying [style] and
 * [visibility], so they are stacked rather than emitted into the parent's scope.
 */
@Triggers([OnSystemBroadcastEventTrigger::class])
@Serializable
@SerialName("SystemBroadcastListener")
@Immutable
data class SystemBroadcastListenerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>
) : TileSchema
