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

/**
 * Renders a single Material Symbol by literal [name] — a thin wrapper over [MaterialSymbol]. Prefer
 * the [IconSchema] overload below when rendering directly from a tile's own `IconSchema` field.
 *
 * @param name Material Symbol name (Google's icon font naming, e.g. `"settings"`, `"delete"`).
 * @param modifier applied to the rendered glyph.
 * @param filled whether to use the filled variant of the icon.
 * @param tint icon color. Defaults to the ambient content color when `null`.
 * @param size icon size. Defaults to the ambient text size when `null`.
 * @param style outline/rounded/sharp glyph style. Defaults to outlined when `null`.
 */
@Composable
fun Icon(
    name: String,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
    tint: Color? = null,
    size: Dp? = null,
    style: MaterialSymbolStyle? = null
) {
    MaterialSymbol(
        iconName = name,
        modifier = modifier,
        filled = filled,
        tint = tint,
        size = size,
        style = style
    )
}

/**
 * Renders an [IconSchema] directly — the composable every built-in icon-bearing tile (`Icon`,
 * `Button.icon`, `AssistChip.leadingIcon`, etc.) uses under the hood, and the one a custom
 * `TileRenderer` should reach for whenever it needs to render an `IconSchema` field.
 *
 * @param schema the icon to render — its own `name`/`color`/`size`/`style` are all read from here.
 * @param filled whether to use the filled variant of the icon — not part of [IconSchema] itself, so
 * it's a separate parameter, `false` by default.
 * @param modifier applied to the rendered glyph.
 */
@Composable
fun Icon(
    schema: IconSchema,
    filled: Boolean = false,
    modifier: Modifier = Modifier,
) {
    with(schema) {
        Icon(
            modifier = modifier,
            filled = filled,
            name = name,
            tint = color?.toComposeColor(),
            size = size?.dp,
            style = style.toMaterialSymbolStyle()
        )
    }
}