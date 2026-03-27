package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTabItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTabItemClickEventTrigger::class
    ]
)
@Serializable
@SerialName("Tabs")
data class TabsTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("selectedTabId") val selectedTabId: String,
    @SerialName("tabItems") val tabItems: List<TabItem>,
    @SerialName("tabType") val tabType: Type,
    @SerialName("scrollable") val scrollable: Boolean
) : TileSchema {

    enum class Type {
        PRIMARY, SECONDARY
    }

    @Serializable
    data class TabItem(
        @SerialName("id") val id: String,
        @SerialName("label") val label: String?,
        @SerialName("icon") val icon: IconSchema?,
        @SerialName("badgeText") val badgeText: String?
    )
}
