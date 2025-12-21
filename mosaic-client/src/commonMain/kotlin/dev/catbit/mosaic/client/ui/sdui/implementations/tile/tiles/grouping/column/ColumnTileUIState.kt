package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.GroupingTileUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState

@Stable
data class ColumnTileUIState(
    val arrangement: Arrangement.Vertical,
    val alignment: Alignment.Horizontal,
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
    override val tiles: List<TileUIState>
) : GroupingTileUIState