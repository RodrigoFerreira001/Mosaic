package dev.catbit.mosaic.core.data.schemas.tile.tiles.menu

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a [Box] that overlays a [DropdownMenu] on top of its [tiles] children. The [tiles]
 * are the anchor content (e.g. an icon button that triggers the menu). The dropdown is shown
 * or hidden based on [expanded]. Each [MenuItem] in [items] is rendered as a
 * [DropdownMenuItem] with a text label and optional leading/trailing icons.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `tiles: List<TileSchema>`, `items: List<MenuItem>`,
 * `expanded: Boolean`
 *
 * **Triggers dispatched:** `OnMenuItemClickEventTrigger` — fired when any menu item is
 * tapped, carrying the clicked item's [id] as the trigger parameter.
 *
 * **Notes:** The open/closed state of the dropdown is server-driven via [expanded]. When the
 * user dismisses the menu by tapping outside it, the renderer dispatches the internal
 * `MenuTileEvents.OnToggleMenu` event, which the client-side tile holder uses to request a
 * server update. The server must then respond with an UpdateTiles setting [expanded] to
 * `false`. The [MenuItem.leadingIcon] and [MenuItem.trailingIcon] are rendered using the
 * icon name string directly.
 */
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