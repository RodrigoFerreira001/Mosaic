package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class NavigationBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val items: NavigationBarItemSchemaBuilderScope.() -> Unit,
    private val selectedItemId: String
) : TileSchemaBuilder<NavigationBarTileSchema>() {

    override fun build() = NavigationBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        items = NavigationBarItemSchemaBuilderScope().apply(items).build(),
        selectedItemId = selectedItemId
    )
}

/**
 * Renders a Material 3 navigation bar with one entry per item declared via [items] (built with
 * `item`). An item is selected when its id equals [selectedItemId]; the selected item's icon is
 * drawn in its filled variant, the others outlined, and its label is centered under the icon (or
 * omitted when not given). Tapping an item flips [selectedItemId] locally on the client (no
 * server round trip needed for the highlight to move) — tapping the already-selected item still
 * fires everything again. The bar itself is not clickable and fires no display trigger; it only
 * tracks the selected item, so performing the actual navigation is up to the events wired to the
 * item clicks. Dispatches `onNavigationBarItemClick(itemId)` when an item is tapped, so events
 * can be wired per item.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onNavigationBarItemClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param selectedItemId Id of the currently selected item.
 * @param items Bar entries, declared with `item`.
 */
fun TileSchemaBuilderScope.NavigationBar(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    selectedItemId: String,
    items: NavigationBarItemSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        NavigationBarTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            items = items,
            selectedItemId = selectedItemId
        )
    )
}

class NavigationBarItemSchemaBuilder(
    private val id: String,
    private val icon: IconSchema,
    private val label: String? = null
) : GenericBuilder<NavigationBarTileSchema.NavigationBarItem>() {

    override fun build() = NavigationBarTileSchema.NavigationBarItem(
        id = id,
        icon = icon,
        label = label
    )
}

class NavigationBarItemSchemaBuilderScope :
    GenericBuilderScope<NavigationBarTileSchema.NavigationBarItem, NavigationBarItemSchemaBuilder>() {

    /**
     * Declares one entry of a `NavigationBar`.
     *
     * @param id Identifier matched against `selectedItemId` and carried by `onNavigationBarItemClick` when this item is tapped.
     * @param icon Icon shown for this item, drawn filled when selected and outlined otherwise.
     * @param label Text shown centered under the icon. Defaults to none (icon only).
     */
    fun item(
        id: String,
        icon: IconSchema,
        label: String? = null
    ) {
        addBuilder(
            NavigationBarItemSchemaBuilder(
                id = id,
                icon = icon,
                label = label
            )
        )
    }
}
