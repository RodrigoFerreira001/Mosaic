package dev.catbit.mosaic.client.ui.foundation.state.tile.style

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.foundation.models.ColorUIModel
import dev.catbit.mosaic.client.ui.foundation.models.WindowInsetsUIModel
import dev.catbit.mosaic.client.ui.foundation.state.UIState

@Stable
data class StyleUIState(
    val size: SizeUIState,
    val margin: PaddingValues? = null,
    val padding: PaddingValues? = null,
    val background: ColorUIModel? = null,
    val border: BorderUIState? = null,
    val clip: ClipUIState? = null,
    val windowInsets: WindowInsetsUIModel? = null
) : UIState










