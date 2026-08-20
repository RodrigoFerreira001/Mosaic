package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationRailItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 `NavigationRail` — the side-rail counterpart of
 * [NavigationBarTileSchema] — with one `NavigationRailItem` per entry in [items], plus 4dp
 * horizontal and 8dp vertical padding applied by the renderer. An item is selected when its
 * [NavigationRailItem.id] equals [selectedItemId]; the selected item's icon is drawn in its
 * **filled** variant, the others outlined. [NavigationRailItem.label] is rendered centered under
 * the icon, or omitted when `null`.
 *
 * **Header and footer:** [header] is rendered in the rail's header slot above the items;
 * [footer] is rendered at the very bottom, pushed down by a weighted spacer so it hugs the
 * bottom edge. Both are arbitrary tiles and both are optional.
 *
 * **Selection:** tapping an item dispatches a local `NavigationRailTileEvents.OnItemClicked` and
 * the holder stores the new [selectedItemId], so the highlight moves immediately without a
 * server round trip. Tapping the already selected item still fires everything again.
 *
 * **Triggers dispatched:**
 * - `OnNavigationRailItemClickEventTrigger` — fired when an item is tapped, carrying that item's
 *   [NavigationRailItem.id], so events can be wired per item.
 *
 * **Notes:** the rail itself is not clickable and fires no display trigger. It only tracks the
 * selected item — performing the actual navigation is up to the events wired to the item clicks.
 */
@Immutable
@Triggers([OnNavigationRailItemClickEventTrigger::class])
@Serializable
@SerialName("NavigationRail")
data class NavigationRailTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("items") val items: SerializableImmutableList<NavigationRailItem>,
    @SerialName("selectedItemId") val selectedItemId: String,
    @SerialName("header") val header: TileSchema? = null,
    @SerialName("footer") val footer: TileSchema? = null
) : TileSchema {

    @Serializable
    data class NavigationRailItem(
        @SerialName("id") val id: String,
        @SerialName("icon") val icon: IconSchema,
        @SerialName("label") val label: String?
    )
}
