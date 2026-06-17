package dev.catbit.mosaic.core.data.responses.screen

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
