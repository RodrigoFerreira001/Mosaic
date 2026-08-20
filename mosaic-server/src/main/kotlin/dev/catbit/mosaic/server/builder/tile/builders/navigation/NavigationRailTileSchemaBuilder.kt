package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class NavigationRailTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val items: NavigationRailItemSchemaBuilderScope.() -> Unit,
    private val selectedItemId: String,
    private val header: (TileSchemaBuilderScope.() -> Unit)? = null,
    private val footer: (TileSchemaBuilderScope.() -> Unit)? = null
) : TileSchemaBuilder<NavigationRailTileSchema>() {

    override fun build() = NavigationRailTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        items = NavigationRailItemSchemaBuilderScope().apply(items).build(),
        selectedItemId = selectedItemId,
        header = header?.let { TileSchemaBuilderScope().apply(it).build().firstOrNull() },
        footer = footer?.let { TileSchemaBuilderScope().apply(it).build().firstOrNull() }
    )
}

/**
 * Renders a Material 3 navigation rail — the side-rail counterpart of `NavigationBar` — with one
 * entry per item declared via [items] (built with `addItem`). An item is selected when its id
 * equals [selectedItemId]; the selected item's icon is drawn in its filled variant, the others
 * outlined, and its label is centered under the icon (or omitted when not given). [header] is
 * rendered above the items and [footer] at the very bottom (pushed down by a weighted spacer so
 * it hugs the bottom edge); both are optional, arbitrary tiles. Tapping an item flips
 * [selectedItemId] locally on the client (no server round trip needed for the highlight to move)
 * — tapping the already-selected item still fires everything again. The rail itself is not
 * clickable and fires no display trigger; it only tracks the selected item, so performing the
 * actual navigation is up to the events wired to the item clicks. Dispatches
 * `onNavigationRailItemClick(itemId)` when an item is tapped, so events can be wired per item.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onNavigationRailItemClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param selectedItemId Id of the currently selected item.
 * @param header Tile rendered in the rail's header slot, above the items. Defaults to none.
 * @param footer Tile rendered at the bottom of the rail. Defaults to none.
 * @param items Rail entries, declared with `addItem`.
 */
fun TileSchemaBuilderScope.NavigationRail(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    selectedItemId: String,
    header: (TileSchemaBuilderScope.() -> Unit)? = null,
    footer: (TileSchemaBuilderScope.() -> Unit)? = null,
    items: NavigationRailItemSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        NavigationRailTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            items = items,
            selectedItemId = selectedItemId,
            header = header,
            footer = footer
        )
    )
}

class NavigationRailItemSchemaBuilder(
    private val id: String,
    private val icon: IconSchema,
    private val label: String? = null
) : GenericBuilder<NavigationRailTileSchema.NavigationRailItem>() {

    override fun build() = NavigationRailTileSchema.NavigationRailItem(
        id = id,
        icon = icon,
        label = label
    )
}

class NavigationRailItemSchemaBuilderScope :
    GenericBuilderScope<NavigationRailTileSchema.NavigationRailItem, NavigationRailItemSchemaBuilder>() {

    /**
     * Declares one entry of a `NavigationRail`.
     *
     * @param id Identifier matched against `selectedItemId` and carried by `onNavigationRailItemClick` when this item is tapped.
     * @param icon Icon shown for this item, drawn filled when selected and outlined otherwise.
     * @param label Text shown centered under the icon. Defaults to none (icon only).
     */
    fun addItem(
        id: String,
        icon: IconSchema,
        label: String? = null
    ) {
        addBuilder(
            NavigationRailItemSchemaBuilder(
                id = id,
                icon = icon,
                label = label
            )
        )
    }
}
