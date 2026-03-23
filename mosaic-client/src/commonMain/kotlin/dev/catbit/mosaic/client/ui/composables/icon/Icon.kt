package dev.catbit.mosaic.client.ui.composables.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.toComposeColor
import dev.catbit.mosaic.client.extensions.toMaterialSymbolStyle
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolStyle
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema

@Composable
fun Icon(
    name: String,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    size: Dp? = null,
    style: MaterialSymbolStyle? = null
) {
    MaterialSymbol(
        modifier = modifier,
        iconName = name,
        tint = tint,
        size = size,
        style = style
    )
}

@Composable
fun Icon(
    schema: IconSchema,
    modifier: Modifier = Modifier,
) {
    with(schema) {
        Icon(
            modifier = modifier,
            name = name,
            tint = color?.toComposeColor(),
            size = size?.dp,
            style = style.toMaterialSymbolStyle()
        )
    }
}