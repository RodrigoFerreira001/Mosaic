package dev.catbit.mosaic.server.builder.screen

import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

class ScreenBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val navigationDrawerTiles: (TileSchemaBuilderScope.() -> Unit)? = null,
    private val tiles: TileSchemaBuilderScope.() -> Unit = {},
) : GenericBuilder<ScreenResponse> {

    override fun build() = ScreenResponse(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        navigationDrawerTiles = navigationDrawerTiles?.let { TileSchemaBuilderScope().apply(it).build() }
    )
}