package dev.catbit.mosaic.client.ui.sdui.foundation.models

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolStyle

@Stable
data class IconUIModel(
    val name: String,
    val color: ColorUIModel? = null,
    val size: Dp? = null,
    val style: MaterialSymbolStyle = MaterialSymbolStyle.OUTLINED
)