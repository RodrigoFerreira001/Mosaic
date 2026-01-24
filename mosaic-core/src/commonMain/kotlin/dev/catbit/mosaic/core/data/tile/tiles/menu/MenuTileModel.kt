package dev.catbit.mosaic.core.data.tile.tiles.menu

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.icon.IconModel
import dev.catbit.mosaic.core.data.tile.GroupingTileModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Menu")
data class MenuTileModel(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("style") override val style: StyleModel,
    @SerialName("visibility") override val visibility: TileModel.Visibility,
    @SerialName("tiles") override val tiles: List<TileModel>,
    @SerialName("items") val items: List<MenuItem>,
    @SerialName("expanded") val expanded: Boolean
) : GroupingTileModel {

    @Serializable
    data class MenuItem(
        @SerialName("id") val id: String,
        @SerialName("label") val label: String,
        @SerialName("leadingIcon") val leadingIcon: IconModel? = null,
        @SerialName("trailingIcon") val trailingIcon: IconModel? = null
    )
}