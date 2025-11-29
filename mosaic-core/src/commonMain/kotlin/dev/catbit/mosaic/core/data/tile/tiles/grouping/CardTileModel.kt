package dev.catbit.mosaic.core.data.tile.tiles.grouping

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.GroupingTileModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Card")
data class CardTileModel(
    @SerialName("id") override val id: String,
    @SerialName("tiles") override val tiles: List<TileModel>,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("style") override val style: StyleModel,
    @SerialName("visibility") override val visibility: TileModel.Visibility,
    @SerialName("kind") val kind: Kind,
) : GroupingTileModel {

    enum class Kind {
        DEFAULT, ELEVATED, OUTLINED
    }
}