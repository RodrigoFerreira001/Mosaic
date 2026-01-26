package dev.catbit.mosaic.server.builder.screen

import dev.catbit.mosaic.core.data.screen.ScreenModel
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.event.EventModelBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileModelBuilderScope

class ScreenBuilder(
    private val id: String,
    private val events: EventModelBuilderScope.() -> Unit = {},
    private val navigationDrawerTiles: (TileModelBuilderScope.() -> Unit)? = null,
    private val tiles: TileModelBuilderScope.() -> Unit = {},
) : GenericBuilder<ScreenModel> {

    override fun build() = ScreenModel(
        id = id,
        events = EventModelBuilderScope().apply(events).build(),
        tiles = TileModelBuilderScope().apply(tiles).build(),
        navigationDrawerTiles = navigationDrawerTiles?.let { TileModelBuilderScope().apply(it).build() }
    )
}