package dev.catbit.mosaic.core.data.schemas.tile.tiles.containers

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.GroupingTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Grid")
data class GridTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") override val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("columns") val columns: Int,
    @SerialName("gutter") val gutter: Int
) : GroupingTileSchema