package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema.CarouselTypeSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class CarouselTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val type: CarouselTypeSchema,
    private val itemSpacing: Int,
    private val contentPadding: Int,
    private val userScrollEnabled: Boolean
) : TileSchemaBuilder<CarouselTileSchema>() {

    override fun build() = CarouselTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        type = type,
        itemSpacing = itemSpacing,
        contentPadding = contentPadding,
        userScrollEnabled = userScrollEnabled
    )
}

/**
 * Renders a Material 3 horizontal carousel over [tiles], one child per item. [type] picks the
 * layout — [multiBrowse] sizes items around a preferred width with shrunken edge items, or
 * [uncontained] uses a fixed item width. [itemSpacing] and [contentPadding] (applied
 * horizontally only) are in dp, and [userScrollEnabled] toggles manual swiping. Listens for
 * scroll-to-begin/end and next/previous-item broadcasts addressed to its [id] — it shares the
 * pager's broadcast, so the same commands work on both tile types. Requesting the next item past
 * the last one (or the previous one before the first) is a no-op. Children are rendered by index
 * with no scope CompositionLocal. Dispatches `onDisplay` once when composed, and
 * `onPageChanged` whenever the current item changes (skipping the initial item) — once per
 * matching direction (any / start / end / that exact index).
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onPageChanged`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param type Item layout strategy — [multiBrowse] or [uncontained].
 * @param itemSpacing Spacing between items, in dp. Defaults to 0.
 * @param contentPadding Horizontal padding applied to the carousel's content, in dp. Defaults to 0.
 * @param userScrollEnabled Whether the user can manually swipe between items. Defaults to true.
 * @param tiles Child tiles rendered one per carousel item.
 */
fun TileSchemaBuilderScope.Carousel(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    type: CarouselTypeSchema,
    itemSpacing: Int = 0,
    contentPadding: Int = 0,
    userScrollEnabled: Boolean = true,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        CarouselTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            type = type,
            itemSpacing = itemSpacing,
            contentPadding = contentPadding,
            userScrollEnabled = userScrollEnabled
        )
    )
}

/**
 * Multi-browse carousel layout — items are sized around [preferredItemWidth], with edge items
 * allowed to shrink between [minSmallItemWidth] and [maxSmallItemWidth] (falling back to
 * Material defaults when left `null`).
 */
fun multiBrowse(
    preferredItemWidth: Int,
    minSmallItemWidth: Int? = null,
    maxSmallItemWidth: Int? = null
): CarouselTypeSchema.MultiBrowse = CarouselTypeSchema.MultiBrowse(
    preferredItemWidth = preferredItemWidth,
    minSmallItemWidth = minSmallItemWidth,
    maxSmallItemWidth = maxSmallItemWidth
)

/** Uncontained carousel layout — every item has the same fixed [itemWidth]. */
fun uncontained(
    itemWidth: Int
): CarouselTypeSchema.Uncontained = CarouselTypeSchema.Uncontained(
    itemWidth = itemWidth
)
