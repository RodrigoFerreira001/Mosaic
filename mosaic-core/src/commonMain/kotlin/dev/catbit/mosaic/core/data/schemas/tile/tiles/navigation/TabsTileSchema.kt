package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTabItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 tab row with the style and scrollability determined by [tabType] and
 * [scrollable]. The four resulting variants are:
 * - `PRIMARY` + non-scrollable → [PrimaryTabRow]
 * - `PRIMARY` + scrollable → [PrimaryScrollableTabRow]
 * - `SECONDARY` + non-scrollable → [SecondaryTabRow]
 * - `SECONDARY` + scrollable → [SecondaryScrollableTabRow]
 *
 * Each [TabItem] can have an optional text label and an optional icon.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `selectedTabId: String`, `tabItems: List<TabItem>`,
 * `tabType: Type`, `scrollable: Boolean`
 *
 * **Triggers dispatched:** `OnTabItemClickEventTrigger` — fired when any tab is tapped,
 * carrying the clicked tab's [id] as the trigger parameter.
 *
 * **Notes:** The selected tab index passed to the Compose component is computed client-side
 * by finding the position of [selectedTabId] within [tabItems]. The selected state is
 * server-driven; the client never updates it locally. Tapping a tab triggers both the event
 * trigger and an internal `TabsTileEvents.OnTabClicked` dispatch. The [TabItem.badgeText]
 * field is defined in the schema but not yet rendered by the current renderer.
 */
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
