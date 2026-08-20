package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import dev.catbit.mosaic.client.ui.composables.icon.Icon
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbolStyle
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema

/** Converts the wire-format [IconSchema.Style] into the [MaterialSymbolStyle] the [Icon] composable
 * expects. */
fun IconSchema.Style.toMaterialSymbolStyle() = when (this) {
    IconSchema.Style.OUTLINED -> MaterialSymbolStyle.OUTLINED
    IconSchema.Style.ROUNDED -> MaterialSymbolStyle.ROUNDED
    IconSchema.Style.SHARP -> MaterialSymbolStyle.SHARP
}

/**
 * Wraps this optional [IconSchema] into a plain, non-clickable `@Composable` lambda rendering it via
 * [Icon] — the usual way a tile with an *optional* icon field (e.g. `AssistChip.leadingIcon`)
 * conditionally renders it without an explicit `if (icon != null)` at every call site.
 *
 * @return a composable rendering this icon, or `null` if the receiver itself is `null`.
 */
fun IconSchema?.iconOrNull(): (@Composable () -> Unit)? = this?.let { { Icon(this) } }

/**
 * Wraps this optional [IconSchema] into a clickable `@Composable` lambda — an [IconButton] wrapping
 * [Icon] and firing [onClick]. Used by tiles like `TextField`, where `clickableLeadingIcon`/
 * `clickableTrailingIcon` decides whether the optional icon should be wrapped this way or rendered
 * plainly via [iconOrNull] instead.
 *
 * @param onClick called when the wrapping [IconButton] is tapped.
 * @return a composable rendering this icon as a tappable button, or `null` if the receiver itself is
 * `null`.
 */
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