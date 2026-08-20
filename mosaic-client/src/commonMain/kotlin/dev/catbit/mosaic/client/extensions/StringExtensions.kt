package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

/**
 * Wraps this optional [String] into a plain `@Composable` lambda rendering it via a Material 3
 * [Text] — the usual way a tile with an *optional* text field (e.g. `TextField.label`/`placeholder`/
 * `supportingText`) conditionally renders it without an explicit `if (value != null)` at every call
 * site.
 *
 * @param centered whether the text is center-aligned instead of the default start alignment.
 * @return a composable rendering this text, or `null` if the receiver itself is `null`.
 */
fun String?.textOrNull(centered: Boolean = false): (@Composable () -> Unit)? =
    this?.let { { Text(this, textAlign = if (centered) TextAlign.Center else TextAlign.Start) } }

/**
 * Parses this string as a hex color — `color(String)`'s underlying implementation. Accepts both a
 * 6-digit `RRGGBB` string (opaque, alpha forced to `FF`) and an 8-digit `AARRGGBB` string as-is; a
 * leading `#` is stripped if present.
 *
 * @return the parsed [Color], or [Color.Unspecified] if this string isn't valid hex.
 */
fun String.toColor(): Color {
    val hex = this.removePrefix("#")
    val color = if (hex.length == 6) "FF$hex" else hex
    return try {
        Color(color.toLong(16))
    } catch (_: Exception) {
        Color.Unspecified
    }
}