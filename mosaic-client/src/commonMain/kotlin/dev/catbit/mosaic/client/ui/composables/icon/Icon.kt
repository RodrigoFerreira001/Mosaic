package dev.catbit.mosaic.client.ui.composables.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.sdui.foundation.models.IconUIModel

@Composable
fun Icon(
    icon: IconUIModel
) {
    with(icon) {
        MaterialSymbol(
            iconName = name,
            tint = color?.toComposeColor() ?: Color.Unspecified,
            size = icon.size ?: 24.dp,
            style = style
        )
    }
}