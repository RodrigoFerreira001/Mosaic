package dev.catbit.mosaic.client.ui.foundation.state.tile.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.foundation.state.UIState

@Stable
data class RadiusUIState(
    val topStart: Dp,
    val topEnd: Dp,
    val bottomStart: Dp,
    val bottomEnd: Dp
) : UIState