package dev.catbit.mosaic.server.builder.tile.builders.search

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class SearchBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val query: String,
    private val placeholder: String?,
    private val leadingIcon: (TileSchemaBuilderScope.() -> Unit)?,
    private val trailingIcon: (TileSchemaBuilderScope.() -> Unit)?
) : TileSchemaBuilder<SearchBarTileSchema>() {

    override fun build() = SearchBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        query = query,
        placeholder = placeholder,
        leadingIcon = leadingIcon?.let { TileSchemaBuilderScope().apply(it).build() }
            ?.firstOrNull(),
        trailingIcon = trailingIcon?.let { TileSchemaBuilderScope().apply(it).build() }
            ?.firstOrNull()
    )
}

/**
 * Renders a Material search field showing [query], with [placeholder] shown when
 * empty. The keyboard's IME action is always "Search". [leadingIcon] is an arbitrary tile
 * rendered in the leading slot. The trailing slot is shared: while [query] is empty it shows
 * [trailingIcon] (if any); as soon as [query] has text it cross-fades to a built-in clear icon
 * button, so a custom trailing icon is never visible at the same time as the clear button. Typing
 * and pressing clear update the query locally on the client (no server round trip needed for the
 * text to change). This is only the input field — it has no expanded state and shows no
 * suggestion list; pair it with e.g. a `LazyColumn` whose `filterChildrenByTerm` is driven by the
 * query to render results. Dispatches `onQueryChanged` (carrying the new text) on every keystroke
 * and also when clear is pressed (with an empty string); `onQueryCleared` when the clear button
 * is pressed, right before the query-changed trigger; and `onSearch` (carrying the current
 * [query]) when the IME "Search" action is pressed.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onQueryChanged`, `onQueryCleared`, `onSearch`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param query Current text shown in the field. Defaults to empty.
 * @param placeholder Text shown when the field is empty. Defaults to none.
 * @param leadingIcon Tile rendered in the leading slot. Defaults to none.
 * @param trailingIcon Tile rendered in the trailing slot while [query] is empty; replaced by a clear button once it has text. Defaults to none.
 */
fun TileSchemaBuilderScope.SearchBar(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    query: String = "",
    placeholder: String? = null,
    leadingIcon: (TileSchemaBuilderScope.() -> Unit)? = null,
    trailingIcon: (TileSchemaBuilderScope.() -> Unit)? = null
) {
    addBuilder(
        SearchBarTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            query = query,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    )
}
