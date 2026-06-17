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
 * Renders a Material 3 [NavigationRail] with a vertical list of destination items defined by
 * [items]. Each item shows an icon (filled when selected) and an optional label. An optional
 * [header] tile is rendered at the top of the rail and an optional [footer] tile is pushed to
 * the bottom via a [Spacer] with `weight(1f)`.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `items: SerializableImmutableList<NavigationRailItem>`,
 * `selectedItemId: String`, `header: TileSchema?`, `footer: TileSchema?`
 *
 * **Triggers dispatched:** `OnNavigationRailItemClickEventTrigger` — fired when any item is
 * tapped, carrying the clicked item's [id] as the trigger parameter.
 *
 * **Notes:** The selected state is server-driven via [selectedItemId]. Tapping an item
 * triggers both the event trigger and an internal `NavigationRailTileEvents.OnItemClicked`
 * dispatch. The renderer applies a fixed `padding(horizontal = 4.dp, vertical = 8.dp)` to the
 * rail regardless of the schema style. The [footer] tile is only rendered if non-null and is
 * always pushed to the bottom of the rail.
 */
@Immutable
@Triggers(
    [
        OnNavigationRailItemClickEventTrigger::class
    ]
)
@Serializable
@SerialName("NavigationRail")
data class NavigationRailTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
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
