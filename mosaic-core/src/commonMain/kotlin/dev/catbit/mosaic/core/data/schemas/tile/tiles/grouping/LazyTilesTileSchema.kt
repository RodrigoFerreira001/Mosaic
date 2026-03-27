package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("LazyTiles")
@Serializable
data class LazyTilesTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("tiles") val tiles: List<TileSchema>? = null,
    @SerialName("placeholderTiles") val placeholderTiles: List<TileSchema>,
    @SerialName("failureTiles") val failureTiles: List<TileSchema>,
    @SerialName("isFailureState") val isFailureState: Boolean = false,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?
) : TileSchema
