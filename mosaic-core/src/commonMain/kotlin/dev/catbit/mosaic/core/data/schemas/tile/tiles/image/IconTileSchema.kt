package dev.catbit.mosaic.core.data.schemas.tile.tiles.image

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Icon")
data class IconTileSchema(
    override val id: String,
    override val events: List<EventSchema>?,
    override val style: StyleSchema,
    override val visibility: TileSchema.Visibility,
    val icon: IconSchema
) : TileSchema
