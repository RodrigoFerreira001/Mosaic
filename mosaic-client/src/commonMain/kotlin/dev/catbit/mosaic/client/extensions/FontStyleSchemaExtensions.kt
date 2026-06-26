package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.font.FontStyle
import dev.catbit.mosaic.core.data.schemas.text.FontStyleSchema

fun FontStyleSchema.toFontStyle(): FontStyle = when (this) {
    FontStyleSchema.NORMAL -> FontStyle.Normal
    FontStyleSchema.ITALIC -> FontStyle.Italic
}
