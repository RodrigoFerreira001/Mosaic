package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyRowTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToTop
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallyToStart
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class LazyRowTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val filterChildrenByTerm: String?,
    private val visibility: TileSchema.Visibility,
    private val arrangement: ArrangementSchema.Horizontal,
    private val alignment: AlignmentSchema.Vertical,
    private val scrollThreshold: Int?,
    private val displayScrollbar: Boolean,
    private val considerLoadingItemAtEndOnThresholdReached: Boolean,
) : TileSchemaBuilder<LazyRowTileSchema>() {

    override fun build() = LazyRowTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        filterChildrenByTerm = filterChildrenByTerm,
        visibility = visibility,
        arrangement = arrangement,
        alignment = alignment,
        scrollThreshold = scrollThreshold,
        considerLoadingItemAtEndOnThresholdReached = considerLoadingItemAtEndOnThresholdReached,
        displayScrollbar = displayScrollbar
    )
}

/**
 * Renders a Compose `LazyRow` over [tiles] — one lazy item per child, keyed by the child's id —
 * spaced by [arrangement] and aligned vertically by [alignment]. Only visible children are
 * composed, so prefer this over `Row` for long or paginated horizontal lists. Publishes each
 * item's lazy scope to its child, so children can use modifiers such as `animateItem` (but not
 * `RowScope.weight`). When [filterChildrenByTerm] is set, only children whose `searchableTerms`
 * contain that term (case-insensitive substring match) are rendered — children without
 * `searchableTerms` are filtered out. When [scrollThreshold] is set, the list fires its
 * threshold trigger once fewer than or exactly that many items remain past the last visible one;
 * it does not fire again until the list grows, and [considerLoadingItemAtEndOnThresholdReached]
 * additionally requires it to grow by more than one item (accounting for a trailing loading
 * placeholder). Listens for scroll-to-start/end/item broadcasts addressed to its [id]. When
 * [displayScrollbar] is true, draws a horizontal scrollbar along the bottom edge (useful on
 * desktop/web). Dispatches `onDisplay` once when composed, `onClick` only when declared,
 * `onScrolled` on scroll direction changes, and the scroll-threshold trigger as described above.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onClick`, `onScrolled`, scroll-threshold).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param filterChildrenByTerm Term used to filter [tiles] by their own `searchableTerms`. Defaults to none (no filtering).
 * @param arrangement Horizontal spacing/positioning of the children. Defaults to arranged to the start.
 * @param alignment Vertical alignment of the children. Defaults to aligned to the top.
 * @param scrollThreshold Item count remaining past the last visible one that fires the scroll-threshold trigger. Defaults to none (pagination disabled).
 * @param considerLoadingItemAtEndOnThresholdReached Whether the list must grow by more than one item (to account for a trailing loading placeholder) before re-firing the threshold trigger. Defaults to true.
 * @param displayScrollbar Whether to draw a horizontal scrollbar along the bottom edge. Defaults to false.
 * @param tiles Child tiles rendered lazily, one per list item.
 */
fun TileSchemaBuilderScope.LazyRow(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    filterChildrenByTerm: String? = null,
    arrangement: ArrangementSchema.Horizontal = arrangeHorizontallyToStart(),
    alignment: AlignmentSchema.Vertical = alignVerticallyToTop(),
    scrollThreshold: Int? = null,
    considerLoadingItemAtEndOnThresholdReached: Boolean = true,
    displayScrollbar: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        LazyRowTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            filterChildrenByTerm = filterChildrenByTerm,
            visibility = visibility,
            arrangement = arrangement,
            alignment = alignment,
            scrollThreshold = scrollThreshold,
            considerLoadingItemAtEndOnThresholdReached = considerLoadingItemAtEndOnThresholdReached,
            displayScrollbar = displayScrollbar
        )
    )
}
