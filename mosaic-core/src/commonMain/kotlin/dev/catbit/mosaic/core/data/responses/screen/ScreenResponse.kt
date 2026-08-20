package dev.catbit.mosaic.core.data.responses.screen

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The raw wire shape of a screen fetch response, decoded directly from the backend's JSON — what
 * `mosaic-server`'s `Screen { }` builder produces. Converted to a
 * [dev.catbit.mosaic.core.data.models.screen.ScreenModel] via `ScreenModel.fromScreenResponse` before
 * the client installs it; that conversion is also where [id]/[ttl] are consumed and dropped —
 * [dev.catbit.mosaic.core.data.models.screen.ScreenModel] carries neither field, since by the time a
 * screen is being installed its id is already known (it's the screen currently being loaded) and its
 * cache freshness has already been decided by the repository layer.
 *
 * @property id id of the screen this response is for.
 * @property tiles the screen's tile tree.
 * @property navigationDrawerTiles content rendered inside the screen's `ModalNavigationDrawer`, if
 * any.
 * @property events screen-level events (e.g. ones triggered by `onDisplay()` at the screen level,
 * not scoped to a single tile).
 * @property ttl how long this response may be served from cache before a fresh fetch is attempted —
 * an ISO date-time string, parsed and consumed only at the repository layer.
 */
@Serializable
data class ScreenResponse(
    @SerialName("id")
    val id: String,
    @SerialName("tiles")
    val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("navigationDrawerTiles")
    val navigationDrawerTiles: SerializableImmutableList<TileSchema>?,
    @SerialName("events")
    val events: SerializableImmutableList<EventSchema>?,
    @SerialName("ttl")
    val ttl: String? = null
)
