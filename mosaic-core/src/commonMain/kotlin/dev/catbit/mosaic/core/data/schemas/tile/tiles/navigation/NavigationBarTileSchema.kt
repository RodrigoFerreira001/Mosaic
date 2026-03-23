package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationBarItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnNavigationBarItemClickEventTrigger::class
    ]
)
@Serializable
@SerialName("NavigationBar")
data class NavigationBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("items") val items: List<NavigationBarItem>,
    @SerialName("selectedItemId") val selectedItemId: String
) : TileSchema {

    @Serializable
    data class NavigationBarItem(
        @SerialName("id") val id: String,
        @SerialName("icon") val icon: IconSchema,
        @SerialName("label") val label: String?
    )
}
