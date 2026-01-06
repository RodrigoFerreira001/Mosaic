package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition

internal object NavigationDrawerTileDefinition : TileDefinition<NavigationDrawerTileModel, NavigationDrawerTileUIState> {
    override val tileModelClass = NavigationDrawerTileModel::class
    override val tileUIStateClass = NavigationDrawerTileUIState::class
    override val tileRenderer = NavigationDrawerTileRenderer
    override val tileUIStateProducerBuilder = NavigationDrawerTileUIStateProducerBuilder
}