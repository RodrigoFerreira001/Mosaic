package dev.catbit.mosaic.client.ui.foundation.state.tile.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Shape
import dev.catbit.mosaic.client.ui.foundation.state.UIState

@Stable
data class ClipUIState(
    val shape: Shape
) : UIState