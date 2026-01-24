package dev.catbit.mosaic.client.ui.composables.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolStyle

@Composable
fun Icon(
    name: String,
    tint: Color = Color.Unspecified,
    size: Dp = 24.dp,
    style: MaterialSymbolStyle = MaterialSymbolStyle.OUTLINED
) {
    MaterialSymbol(
        iconName = name,
        tint = tint,
        size = size,
        style = style
    )

    // TODO utilizar Icon() do compose
}