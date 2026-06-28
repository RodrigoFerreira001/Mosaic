package dev.catbit.mosaic.core.data.schemas.tile.tiles.system

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Transparent container tile that renders its children while hosting system broadcast
 * event listeners.
 *
 * Place this tile anywhere in the screen tile tree to react to `onSystemBroadcast`
 * triggers. Its children are rendered directly without any wrapping layout.
 *
 * **Updatable fields (via UpdateTiles):** `tiles`, `visibility`, `style`.
 *
 * **Triggers dispatched:** None — attach events with `onSystemBroadcast("broadcastId")`
 * to react to system-level broadcasts.
 */
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
