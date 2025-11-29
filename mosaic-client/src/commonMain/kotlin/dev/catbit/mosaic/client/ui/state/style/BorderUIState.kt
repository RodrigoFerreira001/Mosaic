package dev.catbit.mosaic.client.ui.state.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.state.UIState
import dev.catbit.mosaic.client.ui.state.builder.UIStateBuilder

@Stable
data class BorderUIState(
    val color: Color,
    val thickness: Dp,
    val radius: RadiusUIState?
) : UIState

class BorderUIStateBuilder(
    private var color: Color,
    private var thickness: Dp,
    private val radius: RadiusUIStateBuilder
) : UIStateBuilder<BorderUIState>() {

    override fun shouldRebuild() = shouldRebuildWithLastState {
        color != it.color
                || thickness != it.thickness
                || radius.shouldRebuild()
    }

    override fun build() = BorderUIState(
        color = color,
        thickness = thickness,
        radius = radius.state,
    )
}