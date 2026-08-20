package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.style.TextAlign
import dev.catbit.mosaic.core.data.schemas.text.TextAlignSchema

/** Converts the wire-format [TextAlignSchema] into its Compose [TextAlign] counterpart — used by
 * `SimpleText`'s `textAlign`. */
fun TextAlignSchema.toTextAlign(): TextAlign = when (this) {
    TextAlignSchema.LEFT -> TextAlign.Left
    TextAlignSchema.RIGHT -> TextAlign.Right
    TextAlignSchema.CENTER -> TextAlign.Center
    TextAlignSchema.JUSTIFY -> TextAlign.Justify
    TextAlignSchema.START -> TextAlign.Start
    TextAlignSchema.END -> TextAlign.End
}
