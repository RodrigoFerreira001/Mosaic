package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.TextAlignSchema

/** Aligns text to the left edge, regardless of layout direction. */
fun leftTextAlign() = TextAlignSchema.LEFT

/** Aligns text to the right edge, regardless of layout direction. */
fun rightTextAlign() = TextAlignSchema.RIGHT

/** Centers text horizontally. */
fun centerTextAlign() = TextAlignSchema.CENTER

/** Justifies text — stretches each line (except the last) to fill the available width. */
fun justifyTextAlign() = TextAlignSchema.JUSTIFY

/** Aligns text to the start edge (mirrored under RTL). */
fun startTextAlign() = TextAlignSchema.START

/** Aligns text to the end edge (mirrored under RTL). */
fun endTextAlign() = TextAlignSchema.END
