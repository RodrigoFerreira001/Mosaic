package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolStyle
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema

fun IconSchema.Style.toMaterialSymbolStyle() = when (this) {
    IconSchema.Style.OUTLINED -> MaterialSymbolStyle.OUTLINED
    IconSchema.Style.ROUNDED -> MaterialSymbolStyle.ROUNDED
    IconSchema.Style.SHARP -> MaterialSymbolStyle.SHARP
}