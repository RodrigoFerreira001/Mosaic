package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.font.FontFamily
import dev.catbit.mosaic.core.data.schemas.text.FontFamilySchema

fun FontFamilySchema.toFontFamily(): FontFamily = when (this) {
    FontFamilySchema.DEFAULT -> FontFamily.Default
    FontFamilySchema.SERIF -> FontFamily.Serif
    FontFamilySchema.SANS_SERIF -> FontFamily.SansSerif
    FontFamilySchema.MONOSPACE -> FontFamily.Monospace
    FontFamilySchema.CURSIVE -> FontFamily.Cursive
}
