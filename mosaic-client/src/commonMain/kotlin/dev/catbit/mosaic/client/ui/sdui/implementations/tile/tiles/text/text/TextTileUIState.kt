package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState

@Stable
data class TextTileUIState(
    val text: String,
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
) : TileUIState