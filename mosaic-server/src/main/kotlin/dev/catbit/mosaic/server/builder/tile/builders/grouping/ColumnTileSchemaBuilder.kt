package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ColumnTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToStart
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallyToTop
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class ColumnTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val filterChildrenByTerm: String?,
    private val visibility: TileSchema.Visibility,
    private val arrangement: ArrangementSchema.Vertical,
    private val alignment: AlignmentSchema.Horizontal,
    private val scrollable: Boolean,
) : TileSchemaBuilder<ColumnTileSchema>() {

    override fun build() = ColumnTileSchema(
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
 * Renders a Compose `Column` stacking [tiles] vertically, spaced by [arrangement] and aligned
 * horizontally by [alignment]. When [scrollable] is `true` the column becomes vertically
 * scrollable — every child is composed eagerly, so prefer `LazyColumn` for long lists. Publishes
 * its column scope to children so they can use scope modifiers such as `weight`. When
 * [filterChildrenByTerm] is set, only children whose `searchableTerms` contain that term
 * (case-insensitive substring match) are rendered — children without `searchableTerms` are
 * filtered out. Listens for scroll-to-top/bottom/offset broadcasts addressed to its [id].
 * Dispatches `onDisplay` once when composed, `onClick`/`onLongPress` only when declared, and
 * `onScrolled` on scroll direction changes while [scrollable] is true.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onClick`, `onLongPress`, `onScrolled`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param filterChildrenByTerm Term used to filter [tiles] by their own `searchableTerms`. Defaults to none (no filtering).
 * @param arrangement Vertical spacing/positioning of the children. Defaults to arranged to the top.
 * @param alignment Horizontal alignment of the children. Defaults to aligned to the start.
 * @param scrollable Whether the column scrolls vertically when content overflows. Defaults to false.
 * @param tiles Child tiles stacked vertically inside the column.
 */
fun TileSchemaBuilderScope.Column(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    filterChildrenByTerm: String? = null,
    arrangement: ArrangementSchema.Vertical = arrangeVerticallyToTop(),
    alignment: AlignmentSchema.Horizontal = alignHorizontallyToStart(),
    scrollable: Boolean = false,
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        ColumnTileSchemaBuilder(
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
