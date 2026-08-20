package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PullToRefreshTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class PullToRefreshTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val isRefreshing: Boolean
) : TileSchemaBuilder<PullToRefreshTileSchema>() {

    override fun build() = PullToRefreshTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        isRefreshing = isRefreshing
    )
}

/**
 * Renders a Material 3 pull-to-refresh container wrapping [tiles], showing the refresh indicator
 * while [isRefreshing] is `true`. Pulling flips [isRefreshing] to `true` locally on the client
 * (no round trip needed for the spinner to appear) and dispatches `onPull` — hook the refresh
 * work to that trigger. Stopping the indicator is the server's job: chain a
 * `StopRefreshingEventSchema` pointing at this tile's [id] onto the end of the refresh flow, on
 * both the success and failure branch, otherwise it keeps spinning. Children are laid out with
 * `Box` semantics and no scope CompositionLocal; put a scrollable tile inside for the gesture to
 * feel natural. Dispatches `onDisplay` once when composed.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param tiles Child tiles wrapped by the pull-to-refresh container.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, `onPull`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param isRefreshing Whether the refresh indicator is currently shown.
 */
fun TileSchemaBuilderScope.PullToRefresh(
    id: String = randomId(),
    tiles: TileSchemaBuilderScope.() -> Unit,
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    isRefreshing: Boolean = false
) {
    addBuilder(
        PullToRefreshTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            isRefreshing = isRefreshing
        )
    )
}
