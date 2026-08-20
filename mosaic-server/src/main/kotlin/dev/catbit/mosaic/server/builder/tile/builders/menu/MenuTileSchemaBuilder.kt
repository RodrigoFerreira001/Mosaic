package dev.catbit.mosaic.server.builder.tile.builders.menu

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class MenuTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val items: MenuItemSchemaBuilderScope.() -> Unit,
    private val expanded: Boolean
) : TileSchemaBuilder<MenuTileSchema>() {

    override fun build() = MenuTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        items = MenuItemSchemaBuilderScope().apply(items).build(),
        expanded = expanded
    )
}

/**
 * Renders an anchor — [tiles], laid out with `Box` semantics and carrying [style] and
 * [visibility] — with a Material 3 dropdown menu attached to it. The menu lists one entry per
 * item declared via [items] (built with `addMenuItem`), each showing its label plus an optional
 * leading/trailing icon. The menu uses the large theme shape, respects the system bars and is
 * capped at 400dp tall (scrolling beyond that). [expanded] drives the menu; dismissing it
 * (tapping outside or pressing back) flips [expanded] locally on the client, with no server round
 * trip needed. Opening and closing from the server side is done with a `ToggleMenu` event
 * pointing at this tile's [id] — typically wired to the anchor's click to open, and to an item's
 * click chain to close after acting on the selection. [style] and [visibility] apply to the
 * anchor only — the dropdown itself is positioned by Material and unaffected by them. Dispatches
 * `onMenuItemClick(itemId)` when an item is tapped, so events can be wired per item.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param style Layout/appearance modifiers applied to the anchor (size, padding, background, etc).
 * @param visibility Whether the anchor is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param expanded Whether the dropdown menu starts open. Defaults to false.
 * @param items Menu entries, declared with `addMenuItem`.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onMenuItemClick`).
 * @param tiles Child tiles forming the clickable anchor the menu is attached to.
 */
fun TileSchemaBuilderScope.Menu(
    id: String = randomId(),
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    expanded: Boolean = false,
    items: MenuItemSchemaBuilderScope.() -> Unit,
    events: EventSchemaBuilderScope.() -> Unit = {},
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        MenuTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            items = items,
            expanded = expanded
        )
    )
}

class MenuItemSchemaBuilder(
    private val id: String,
    private val label: String,
    private val leadingIcon: IconSchema? = null,
    private val trailingIcon: IconSchema? = null
) : GenericBuilder<MenuTileSchema.MenuItem>() {

    override fun build() = MenuTileSchema.MenuItem(
        id = id,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

class MenuItemSchemaBuilderScope :
    GenericBuilderScope<MenuTileSchema.MenuItem, MenuItemSchemaBuilder>() {

    /**
     * Declares one entry of a `Menu`'s dropdown.
     *
     * @param id Identifier carried by `onMenuItemClick` when this item is tapped, so events can be wired per item.
     * @param label Text shown for this item.
     * @param leadingIcon Optional icon rendered before the label. Defaults to none.
     * @param trailingIcon Optional icon rendered after the label. Defaults to none.
     */
    fun addMenuItem(
        id: String,
        label: String,
        leadingIcon: IconSchema? = null,
        trailingIcon: IconSchema? = null
    ) {
        addBuilder(
            MenuItemSchemaBuilder(
                id = id,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon
            )
        )
    }
}
