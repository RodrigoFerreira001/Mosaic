package dev.catbit.mosaic.core.data.schemas.tile.tiles.text

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Text")
data class TextTileSchema(
    override val id: String,
    override val events: List<EventSchema>?,
    override val style: StyleSchema,
    override val visibility: TileSchema.Visibility,
    val text: String
) : TileSchema