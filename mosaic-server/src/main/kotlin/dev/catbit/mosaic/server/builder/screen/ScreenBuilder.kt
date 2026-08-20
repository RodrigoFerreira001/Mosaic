package dev.catbit.mosaic.server.builder.screen

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import kotlinx.datetime.LocalDateTime

/** Compiles a standalone screen's [tiles]/[events]/[navigationDrawerTiles] into a [ScreenResponse]. */
class ScreenBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val navigationDrawerTiles: (TileSchemaBuilderScope.() -> Unit)? = null,
    private val tiles: TileSchemaBuilderScope.() -> Unit = {},
    private val ttl: LocalDateTime? = null,
) : GenericBuilder<ScreenResponse>() {

    override fun build() = ScreenResponse(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        navigationDrawerTiles = navigationDrawerTiles?.let { TileSchemaBuilderScope().apply(it).build() },
        ttl = ttl?.toString()
    )
}

/**
 * Builds a standalone screen — the top-level entry point for a non-graph endpoint, returning the
 * [ScreenResponse] the client fetches and renders. For a screen that lives inside a back stack,
 * use `entry` under [dev.catbit.mosaic.server.builder.graph.Graph] instead.
 *
 * @param id Unique identifier of the screen, referenced by `Navigate(destination = id)`.
 * @param events Screen-level events, e.g. an initial `GetData`/`SendNetworkRequest` run on display.
 * @param navigationDrawerTiles Tiles rendered inside a `ModalNavigationDrawer` for this screen. Defaults to none (no drawer).
 * @param ttl Cache expiry — the client re-fetches this screen after this datetime instead of using its cached copy. Defaults to none (cached indefinitely).
 * @param tiles Tile tree rendered as the screen's content.
 */
fun Screen(
    id: String,
    events: EventSchemaBuilderScope.() -> Unit = {},
    navigationDrawerTiles: (TileSchemaBuilderScope.() -> Unit)? = null,
    ttl: LocalDateTime? = null,
    tiles: TileSchemaBuilderScope.() -> Unit = {},
) = ScreenBuilder(
    id = id,
    events = events,
    navigationDrawerTiles = navigationDrawerTiles,
    tiles = tiles,
    ttl = ttl
).build()