package dev.catbit.mosaic.client.extensions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

fun String?.textOrNull(centered: Boolean = false): (@Composable () -> Unit)? =
    this?.let { { Text(this, textAlign = if (centered) TextAlign.Center else TextAlign.Start) } }

fun String.toColor(): Color {
    val hex = this.removePrefix("#")
    val color = if (hex.length == 6) "FF$hex" else hex
    return try {
        Color(color.toLong(16))
    } catch (_: Exception) {
        Color.Unspecified
    }
}