package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu

import dev.catbit.mosaic.client.ui.sdui.foundation.models.IconUIModel
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.GroupingTileUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState

data class MenuTileUIState(
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
    override val tiles: List<TileUIState>,
    val items: List<MenuItem>,
    val expanded: Boolean
) : GroupingTileUIState {

    data class MenuItem(
        val id: String,
        val label: String,
        val leadingIcon: IconUIModel?,
        val trailingIcon: IconUIModel?
    )
}
