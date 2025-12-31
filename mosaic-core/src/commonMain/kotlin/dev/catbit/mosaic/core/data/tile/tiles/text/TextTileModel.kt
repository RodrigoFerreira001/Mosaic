package dev.catbit.mosaic.core.data.tile.tiles.text

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Text")
data class TextTileModel(
    override val id: String,
    override val events: List<EventModel>?,
    override val style: StyleModel,
    override val visibility: TileModel.Visibility,
    val text: String
) : TileModel