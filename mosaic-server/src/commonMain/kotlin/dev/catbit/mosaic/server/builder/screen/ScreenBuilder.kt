package dev.catbit.mosaic.server.builder.screen

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import kotlinx.datetime.LocalDateTime

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