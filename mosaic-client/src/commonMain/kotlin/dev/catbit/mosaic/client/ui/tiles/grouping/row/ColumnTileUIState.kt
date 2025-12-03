package dev.catbit.mosaic.client.ui.tiles.grouping.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import dev.catbit.mosaic.client.ui.foundation.state.tile.GroupingTileUIState
import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.foundation.state.tile.style.StyleUIState

@Stable
data class ColumnTileUIState(
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
    override val tiles: List<TileUIState>,
    val arrangement: Arrangement.Vertical,
    val alignment: Alignment.Horizontal
) : GroupingTileUIState