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
    override val id: String,
    override val events: List<EventModel>?,
    override val style: StyleModel,
    override val visibility: TileModel.Visibility,
    override val tiles: List<TileModel>,
    val items: List<MenuItem>
) : GroupingTileModel {

    @Serializable
    data class MenuItem(
        @SerialName("id") val id: String,
        @SerialName("label") val label: String,
        @SerialName("leadingIcon") val leadingIcon: IconModel? = null,
        @SerialName("trailingIcon") val trailingIcon: IconModel? = null
    )
}