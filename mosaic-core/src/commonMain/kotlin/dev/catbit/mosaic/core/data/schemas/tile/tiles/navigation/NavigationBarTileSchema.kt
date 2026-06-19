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
 * Renders a Material 3 [NavigationBar] with a fixed set of destination items defined by
 * [items]. Each item displays an icon (filled when selected, outlined otherwise) and an
 * optional text label. The currently selected item is identified by [selectedItemId].
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `items: SerializableImmutableList<NavigationBarItem>`,
 * `selectedItemId: String`
 *
 * **Triggers dispatched:** `OnNavigationBarItemClickEventTrigger` — fired when any item is
 * tapped, carrying the clicked item's [id] as the trigger parameter.
 *
 * **Notes:** The selected state is entirely server-driven via [selectedItemId]; the client
 * never updates selection state locally. Tapping an item triggers both the event trigger
 * (for server-side event runners) and an internal `NavigationBarTileEvents.OnItemClicked`
 * dispatch (for client-side tile holder logic). The icon filling (filled vs. outlined) is
 * determined by comparing each item's [id] against [selectedItemId] at render time.
 */
@Immutable
@Triggers(
    [
        OnNavigationBarItemClickEventTrigger::class
    ]
)
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
