package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolStyle
import dev.catbit.mosaic.core.data.icon.IconModel

fun IconModel.Style.toMaterialSymbolStyle() = when (this) {
    IconModel.Style.OUTLINED -> MaterialSymbolStyle.OUTLINED
    IconModel.Style.ROUNDED -> MaterialSymbolStyle.ROUNDED
    IconModel.Style.SHARP -> MaterialSymbolStyle.SHARP
}