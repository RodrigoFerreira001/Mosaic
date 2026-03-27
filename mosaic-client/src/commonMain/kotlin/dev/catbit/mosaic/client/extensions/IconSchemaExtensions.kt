package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolStyle
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema

fun IconSchema.Style.toMaterialSymbolStyle() = when (this) {
    IconSchema.Style.OUTLINED -> MaterialSymbolStyle.OUTLINED
    IconSchema.Style.ROUNDED -> MaterialSymbolStyle.ROUNDED
    IconSchema.Style.SHARP -> MaterialSymbolStyle.SHARP
}

fun IconSchema?.iconOrNull(): (@Composable () -> Unit)? = this?.let { { Icon(this) } }

fun IconSchema?.iconButtonOrNull(
    onClick: () -> Unit,
): (@Composable () -> Unit)? = this?.let {
    {
        IconButton(
            onClick = onClick,
        ) {
            Icon(this)
        }
    }
}