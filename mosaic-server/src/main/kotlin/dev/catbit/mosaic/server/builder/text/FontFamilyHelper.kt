package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.FontFamilySchema

/** Platform default font family. */
fun defaultFontFamily() = FontFamilySchema.DEFAULT

/** Serif font family (e.g. Times-like, with letter strokes). */
fun serifFontFamily() = FontFamilySchema.SERIF

/** Sans-serif font family (e.g. Roboto-like, no letter strokes). */
fun sansSerifFontFamily() = FontFamilySchema.SANS_SERIF

/** Monospace font family — every character occupies the same width. */
fun monospaceFontFamily() = FontFamilySchema.MONOSPACE

/** Cursive/handwriting-style font family. */
fun cursiveFontFamily() = FontFamilySchema.CURSIVE
