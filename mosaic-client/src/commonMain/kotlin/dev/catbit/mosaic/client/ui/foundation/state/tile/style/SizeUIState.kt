package dev.catbit.mosaic.client.ui.foundation.state.tile.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.foundation.state.UIState

@Stable
data class SizeUIState(
    val width: Behavior.Horizontal,
    val height: Behavior.Vertical
) : UIState {
    sealed interface Behavior {

        sealed interface Horizontal : Behavior {

            data object Fill : Horizontal
            data object Wrap : Horizontal
            data class Fixed(val value: Dp) : Horizontal
            data class Weight(val value: Float) : Horizontal
            data class Span(val value: Int) : Horizontal
        }

        sealed interface Vertical : Behavior {

            data object Fill : Vertical
            data object Wrap : Vertical
            data class Fixed(val value: Dp) : Vertical
            data class Weight(val value: Float) : Vertical
        }
    }
}

