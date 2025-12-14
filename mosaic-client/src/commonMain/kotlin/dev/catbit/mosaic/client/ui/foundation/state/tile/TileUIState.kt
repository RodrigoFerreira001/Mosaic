package dev.catbit.mosaic.client.ui.foundation.state.tile

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.foundation.state.UIState
import dev.catbit.mosaic.client.ui.base_implementations.tile.style.StyleUIState

@Stable
interface TileUIState : UIState {
    val id: String
    val style: StyleUIState
    val visibility: Visibility

    enum class Visibility {
        VISIBLE, INVISIBLE, GONE
    }
}