package dev.catbit.mosaic.client.ui.foundation.state.tile.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.foundation.state.UIState
import dev.catbit.mosaic.client.ui.foundation.models.ColorUIModel

@Stable
data class BorderUIState(
    val color: ColorUIModel,
    val thickness: Dp,
    val radius: RadiusUIState?
) : UIState