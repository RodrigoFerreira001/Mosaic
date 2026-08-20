package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.TextDecorationSchema

/** No text decoration. */
fun noneTextDecoration() = TextDecorationSchema.NONE

/** Draws a line under the text. */
fun underlineTextDecoration() = TextDecorationSchema.UNDERLINE

/** Draws a line through the middle of the text (strikethrough). */
fun lineThroughTextDecoration() = TextDecorationSchema.LINE_THROUGH
