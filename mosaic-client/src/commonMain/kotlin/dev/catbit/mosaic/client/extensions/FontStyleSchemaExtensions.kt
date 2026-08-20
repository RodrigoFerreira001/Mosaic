package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.font.FontStyle
import dev.catbit.mosaic.core.data.schemas.text.FontStyleSchema

/** Converts the wire-format [FontStyleSchema] into its Compose [FontStyle] counterpart — used
 * wherever `SimpleText`/`TextField` resolve `fontStyle`. */
fun FontStyleSchema.toFontStyle(): FontStyle = when (this) {
    FontStyleSchema.NORMAL -> FontStyle.Normal
    FontStyleSchema.ITALIC -> FontStyle.Italic
}
