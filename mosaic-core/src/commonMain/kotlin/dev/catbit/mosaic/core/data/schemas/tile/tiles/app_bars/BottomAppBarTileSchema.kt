package dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("BottomAppBar")
data class BottomAppBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("actions") val actions: List<TileSchema>,
    @SerialName("floatingActionButton") val floatingActionButton: TileSchema? = null
) : TileSchema