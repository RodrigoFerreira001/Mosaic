package dev.catbit.mosaic.server.builder.tile.builders.app_bars

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class TopAppBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val title: TileSchemaBuilderScope.() -> Unit,
    private val navigationIcon: (TileSchemaBuilderScope.() -> Unit)?,
    private val actions: (TileSchemaBuilderScope.() -> Unit)?,
    private val barStyle: TopAppBarTileSchema.TopAppBarStyle
) : TileSchemaBuilder<TopAppBarTileSchema>() {

    override fun build() = TopAppBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        title = TileSchemaBuilderScope().apply(title).build().lastOrNull()
            ?: throw IllegalArgumentException("TopAppBar requires at least on title"),
        navigationIcon = navigationIcon?.let { TileSchemaBuilderScope().apply(it).build().lastOrNull() },
        actions = actions?.let { TileSchemaBuilderScope().apply(it).build() },
        barStyle = barStyle
    )
}

/**
 * Renders a Material 3 top app bar. [barStyle] picks the visual variant — default, center
 * aligned, medium or large (the medium/large variants do not collapse on scroll, since the bar
 * has no scroll behavior wired). [title] fills the title slot, [navigationIcon] the leading slot
 * and [actions] the trailing slot; the latter two render as empty slots when omitted. The bar
 * itself dispatches no triggers and is not clickable — wire events on the slot tiles (typically
 * `IconButton`s) instead.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile. Never fired, since the bar dispatches no triggers.
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param navigationIcon Tile rendered in the leading slot (typically an `IconButton`). Defaults to none.
 * @param actions Tiles rendered in the trailing slot. Defaults to none.
 * @param barStyle Visual variant of the bar — [defaultTopAppBar], [centerAlignedTopAppBar], [mediumTopAppBar] or [largeTopAppBar]. Defaults to default.
 * @param title Tile rendered in the title slot (most often a `SimpleText`). Required.
 */
fun TileSchemaBuilderScope.TopAppBar(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    navigationIcon: (TileSchemaBuilderScope.() -> Unit)? = null,
    actions: (TileSchemaBuilderScope.() -> Unit)? = null,
    barStyle: TopAppBarTileSchema.TopAppBarStyle = defaultTopAppBar(),
    title: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        TopAppBarTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            barStyle = barStyle
        )
    )
}

/** Default top app bar variant — leading-aligned title. */
fun defaultTopAppBar() = TopAppBarTileSchema.TopAppBarStyle.DEFAULT

/** Center-aligned top app bar variant — title centered in the bar. */
fun centerAlignedTopAppBar() = TopAppBarTileSchema.TopAppBarStyle.CENTER_ALIGNED

/** Medium top app bar variant — taller bar with a larger title. */
fun mediumTopAppBar() = TopAppBarTileSchema.TopAppBarStyle.MEDIUM

/** Large top app bar variant — tallest bar with the largest title. */
fun largeTopAppBar() = TopAppBarTileSchema.TopAppBarStyle.LARGE