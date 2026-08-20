package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.RowTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToTop
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallyToStart
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class RowTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val filterChildrenByTerm: String?,
    private val visibility: TileSchema.Visibility,
    private val arrangement: ArrangementSchema.Horizontal,
    private val alignment: AlignmentSchema.Vertical,
    private val scrollable: Boolean,
) : TileSchemaBuilder<RowTileSchema>() {

    override fun build() = RowTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        filterChildrenByTerm = filterChildrenByTerm,
        visibility = visibility,
        arrangement = arrangement,
        alignment = alignment,
        scrollable = scrollable,
    )
}

/**
 * Renders a Compose `Row` laying [tiles] out horizontally, spaced by [arrangement] and aligned
 * vertically by [alignment]. When [scrollable] is `true` the row becomes horizontally
 * scrollable — every child is composed eagerly, so prefer `LazyRow` for long lists. Publishes
 * its row scope to children so they can use scope modifiers such as `weight`. When
 * [filterChildrenByTerm] is set, only children whose `searchableTerms` contain that term
 * (case-insensitive substring match) are rendered — children without `searchableTerms` are
 * filtered out. Listens for scroll-to-start/end/offset broadcasts addressed to its [id].
 * Dispatches `onDisplay` once when composed, `onClick`/`onLongPress` only when declared, and
 * `onScrolled` on scroll direction changes while [scrollable] is true.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onClick`, `onLongPress`, `onScrolled`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param filterChildrenByTerm Term used to filter [tiles] by their own `searchableTerms`. Defaults to none (no filtering).
 * @param arrangement Horizontal spacing/positioning of the children. Defaults to arranged to the start.
 * @param alignment Vertical alignment of the children. Defaults to aligned to the top.
 * @param scrollable Whether the row scrolls horizontally when content overflows. Defaults to false.
 * @param tiles Child tiles laid out horizontally inside the row.
 */
fun TileSchemaBuilderScope.Row(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    filterChildrenByTerm: String? = null,
    arrangement: ArrangementSchema.Horizontal = arrangeHorizontallyToStart(),
    alignment: AlignmentSchema.Vertical = alignVerticallyToTop(),
    scrollable: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        RowTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            filterChildrenByTerm = filterChildrenByTerm,
            visibility = visibility,
            arrangement = arrangement,
            alignment = alignment,
            scrollable = scrollable,
        )
    )
}
