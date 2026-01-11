package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.bottom_sheet

import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.GroupingTileUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState

data class BottomSheetTileUIState(
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
    override val tiles: List<TileUIState>
) : GroupingTileUIState