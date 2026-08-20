package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema.PageSizeSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class PagerTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val pageSize: PageSizeSchema,
    private val pageSpacing: Int,
    private val contentPadding: Int,
    private val beyondViewportPageCount: Int
) : TileSchemaBuilder<PagerTileSchema>() {

    override fun build() = PagerTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        pageSize = pageSize,
        pageSpacing = pageSpacing,
        contentPadding = contentPadding,
        beyondViewportPageCount = beyondViewportPageCount
    )
}

/**
 * Renders a horizontal-only Compose `HorizontalPager` over [tiles], one page per child.
 * [pageSize] chooses between full-width pages ([pageFill]) and fixed-width pages ([pageFixed],
 * in dp); [pageSpacing] and [contentPadding] (horizontal only) are dp values, and
 * [beyondViewportPageCount] controls how many off-screen pages stay composed. Listens for
 * scroll-to-begin/end and next/previous-page broadcasts addressed to its [id] — requesting the
 * next page past the last one (or the previous one before the first) is a no-op. Children are
 * rendered by index with no scope CompositionLocal. Dispatches `onDisplay` once when composed,
 * and `onPageChanged` whenever the settled current page changes (skipping the initial page) —
 * once per matching direction (any / start / end / that exact index); wiring several directions
 * runs several chains for the same page change.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onPageChanged`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param pageSize Page width strategy — [pageFill] (full width) or [pageFixed] (fixed dp width). Defaults to fill.
 * @param pageSpacing Spacing between pages, in dp. Defaults to 0.
 * @param contentPadding Horizontal padding applied to the pager's content, in dp. Defaults to 0.
 * @param beyondViewportPageCount Number of off-screen pages kept composed on each side. Defaults to 0.
 * @param tiles Child tiles rendered one per page.
 */
fun TileSchemaBuilderScope.Pager(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    pageSize: PageSizeSchema = pageFill(),
    pageSpacing: Int = 0,
    contentPadding: Int = 0,
    beyondViewportPageCount: Int = 0,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        PagerTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            pageSize = pageSize,
            pageSpacing = pageSpacing,
            contentPadding = contentPadding,
            beyondViewportPageCount = beyondViewportPageCount
        )
    )
}

/** Pages fill the full available width. */
fun pageFill(): PageSizeSchema = PageSizeSchema.Fill

/** Pages take a fixed width, in dp. */
fun pageFixed(value: Int): PageSizeSchema = PageSizeSchema.Fixed(value)
