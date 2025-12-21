package dev.catbit.mosaic.client.ui.sdui.foundation.state.tile

import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.state.UIState

@Stable
interface TileUIState : UIState {
    val id: String
    val style: StyleUIState
    val visibility: Visibility

    enum class Visibility {
        VISIBLE, INVISIBLE, GONE
    }

    fun isVisible() = visibility == Visibility.VISIBLE
    fun isGone() = visibility == Visibility.GONE
}