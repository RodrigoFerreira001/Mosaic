package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.FontStyleSchema

/** Upright (non-italic) font style. */
fun normalFontStyle() = FontStyleSchema.NORMAL

/** Italic (slanted) font style. */
fun italicFontStyle() = FontStyleSchema.ITALIC
