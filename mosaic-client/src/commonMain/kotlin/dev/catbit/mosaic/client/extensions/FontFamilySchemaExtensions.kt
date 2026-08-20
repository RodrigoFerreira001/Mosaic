package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.font.FontFamily
import dev.catbit.mosaic.core.data.schemas.text.FontFamilySchema

/** Converts the wire-format [FontFamilySchema] into its Compose [FontFamily] counterpart — used
 * wherever `SimpleText`/`TextField` and similar text-bearing tiles resolve `fontFamily`. */
fun FontFamilySchema.toFontFamily(): FontFamily = when (this) {
    FontFamilySchema.DEFAULT -> FontFamily.Default
    FontFamilySchema.SERIF -> FontFamily.Serif
    FontFamilySchema.SANS_SERIF -> FontFamily.SansSerif
    FontFamilySchema.MONOSPACE -> FontFamily.Monospace
    FontFamilySchema.CURSIVE -> FontFamily.Cursive
}
