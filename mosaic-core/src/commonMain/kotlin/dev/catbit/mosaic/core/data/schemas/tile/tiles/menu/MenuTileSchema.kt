package dev.catbit.mosaic.core.data.schemas.tile.tiles.menu

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Menu")
data class MenuTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("items") val items: List<MenuItem>,
    @SerialName("expanded") val expanded: Boolean
) : TileSchema {

    @Serializable
    data class MenuItem(
        @SerialName("id") val id: String,
        @SerialName("label") val label: String,
        @SerialName("leadingIcon") val leadingIcon: IconSchema? = null,
        @SerialName("trailingIcon") val trailingIcon: IconSchema? = null
    )
}