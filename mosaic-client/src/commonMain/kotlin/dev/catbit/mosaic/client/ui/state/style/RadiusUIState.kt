package dev.catbit.mosaic.client.ui.state.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.state.UIState
import dev.catbit.mosaic.client.ui.state.builder.UIStateBuilder

@Stable
data class RadiusUIState(
    val topStart: Dp,
    val topEnd: Dp,
    val bottomStart: Dp,
    val bottomEnd: Dp
) : UIState

class RadiusUIStateBuilder(
    private var topStart: Dp,
    private var topEnd: Dp,
    private var bottomStart: Dp,
    private var bottomEnd: Dp
) : UIStateBuilder<RadiusUIState>() {

    override fun shouldRebuild() = shouldRebuildWithLastState {
        topStart != it.topStart
                || topEnd != it.topEnd
                || bottomStart != it.bottomStart
                || bottomEnd != it.bottomEnd
    }

    override fun build() = RadiusUIState(
        topStart = topStart,
        topEnd = topEnd,
        bottomStart = bottomStart,
        bottomEnd = bottomEnd,
    )
}