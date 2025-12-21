package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState

@Stable
data class ButtonTileUIState(
    val text: String,
    val loading: Boolean,
    override val id: String,
    override val style: StyleUIState,
    override val visibility: TileUIState.Visibility,
) : TileUIState