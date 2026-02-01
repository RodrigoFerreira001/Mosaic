package dev.catbit.mosaic.core.data.models.screen

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenModel(
    @SerialName("id")
    val id: String,
    @SerialName("tiles")
    val tiles: List<TileSchema>,
    @SerialName("navigationDrawerTiles")
    val navigationDrawerTiles: List<TileSchema>?,
    @SerialName("events")
    val events: List<EventSchema>?
) {

    companion object {
        fun fromScreenResponse(
            screenResponse: ScreenResponse
        ) = ScreenModel(
            id = screenResponse.id,
            tiles = screenResponse.tiles,
            navigationDrawerTiles = screenResponse.navigationDrawerTiles,
            events = screenResponse.events,
        )
    }
}
