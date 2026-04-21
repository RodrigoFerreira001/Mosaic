package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAdaptiveNavigationItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnAdaptiveNavigationItemClickEventTrigger::class,
        OnNavigationEntryChangedEventTrigger::class,
        OnNavigationEntrySetEventTrigger::class,
    ]
)
@Serializable
@SerialName("AdaptiveNavigation")
data class AdaptiveNavigationTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("navigatorId") val navigatorId: String,
    @SerialName("entries") val entries: List<NavigationEntry>,
    @SerialName("startEntryId") val startEntryId: String,
    @SerialName("selectedEntryId") val selectedEntryId: String,
    @SerialName("header") val header: TileSchema? = null,
    @SerialName("footer") val footer: TileSchema? = null,
) : TileSchema {

    @Serializable
    data class NavigationEntry(
        @SerialName("id") val id: String,
        @SerialName("icon") val icon: IconSchema,
        @SerialName("label") val label: String?,
        @SerialName("initialTiles") val initialTiles: List<TileSchema>,
        @SerialName("initialEvents") val initialEvents: List<EventSchema>,
        @SerialName("failureTiles") val failureTiles: List<TileSchema>,
        @SerialName("failureEvents") val failureEvents: List<EventSchema>,
    )
}
