package dev.catbit.mosaic.client.ui.state.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Shape
import dev.catbit.mosaic.client.ui.state.UIState
import dev.catbit.mosaic.client.ui.state.builder.UIStateBuilder

@Stable
data class ClipUIState(
    val shape: Shape
) : UIState

class ClipUIStateBuilder(
    var shape: Shape
) : UIStateBuilder<ClipUIState>() {

    override fun shouldRebuild() = shouldRebuildWithLastState {
        shape != it.shape
    }

    override fun build() = ClipUIState(
        shape = shape
    )
}