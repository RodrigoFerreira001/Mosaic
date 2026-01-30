package dev.catbit.mosaic.core.data.responses.screen

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenResponse(
    @SerialName("id")
    val id: String,
    @SerialName("tiles")
    val tiles: List<TileSchema>,
    @SerialName("navigationDrawerTiles")
    val navigationDrawerTiles: List<TileSchema>?,
    @SerialName("events")
    val events: List<EventSchema>?,
    @SerialName("ttl")
    val ttl: String? = null
)
