package dev.catbit.mosaic.core.data.models.screen

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The app-facing screen payload — [dev.catbit.mosaic.core.data.responses.screen.ScreenResponse]
 * with [dev.catbit.mosaic.core.data.responses.screen.ScreenResponse.id]/`ttl` already consumed by the
 * repository layer and dropped. This is what `GetScreen`'s `onSuccess` carries as `incomingData`,
 * and what `ChangeScreenState`'s `Success` state installs onto a screen.
 *
 * @property tiles the screen's tile tree.
 * @property navigationDrawerTiles content rendered inside the screen's `ModalNavigationDrawer`, if
 * any.
 * @property events screen-level events.
 */
@Serializable
data class ScreenModel(
    @SerialName("tiles")
    val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("navigationDrawerTiles")
    val navigationDrawerTiles: SerializableImmutableList<TileSchema>?,
    @SerialName("events")
    val events: SerializableImmutableList<EventSchema>?
) {

    companion object {
        /** Converts a [ScreenResponse] into a [ScreenModel], dropping [ScreenResponse.id]/`ttl`
         * (both already consumed by the repository layer by this point). */
        fun fromScreenResponse(
            screenResponse: ScreenResponse
        ) = ScreenModel(
            tiles = screenResponse.tiles,
            navigationDrawerTiles = screenResponse.navigationDrawerTiles,
            events = screenResponse.events,
        )
    }
}
