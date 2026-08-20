package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationBarItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 `NavigationBar` with one `NavigationBarItem` per entry in [items]. An
 * item is selected when its [NavigationBarItem.id] equals [selectedItemId]; the selected item's
 * icon is drawn in its **filled** variant, the others outlined. [NavigationBarItem.label] is
 * rendered centered under the icon, or omitted when `null`.
 *
 * **Selection:** tapping an item dispatches a local `NavigationBarTileEvents.OnItemClicked` and
 * the holder stores the new [selectedItemId], so the highlight moves immediately without a
 * server round trip. Tapping the already selected item still fires everything again.
 *
 * **Triggers dispatched:**
 * - `OnNavigationBarItemClickEventTrigger` — fired when an item is tapped, carrying that item's
 *   [NavigationBarItem.id], so events can be wired per item.
 *
 * **Notes:** the bar itself is not clickable and fires no display trigger. It only tracks the
 * selected item — performing the actual navigation is up to the events wired to the item clicks.
 */
@Immutable
@Triggers([OnNavigationBarItemClickEventTrigger::class])
@Serializable
@SerialName("NavigationBar")
data class NavigationBarTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("items") val items: SerializableImmutableList<NavigationBarItem>,
    @SerialName("selectedItemId") val selectedItemId: String
) : TileSchema {

    @Serializable
    data class NavigationBarItem(
        @SerialName("id") val id: String,
        @SerialName("icon") val icon: IconSchema,
        @SerialName("label") val label: String?
    )
}
