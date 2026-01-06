package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.navigation_drawer

import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.GroupingTileUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState

data class NavigationDrawerTileUIState(
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
    override val tiles: List<TileUIState>
) : GroupingTileUIState