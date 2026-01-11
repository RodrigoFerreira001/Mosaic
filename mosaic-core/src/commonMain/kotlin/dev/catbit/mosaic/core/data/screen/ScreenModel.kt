package dev.catbit.mosaic.core.data.screen

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO ttl, cache, local
@Serializable
data class ScreenModel(
    @SerialName("tiles")
    val tiles: List<TileModel>,
    @SerialName("navigationDrawer")
    val navigationDrawer: List<TileModel>?,
    @SerialName("events")
    val events: List<EventModel>?
)
