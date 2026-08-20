package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.style.TextDecoration
import dev.catbit.mosaic.core.data.schemas.text.TextDecorationSchema

/** Converts the wire-format [TextDecorationSchema] into its Compose [TextDecoration] counterpart —
 * used by `SimpleText`'s `textDecoration`. */
fun TextDecorationSchema.toTextDecoration(): TextDecoration = when (this) {
    TextDecorationSchema.NONE -> TextDecoration.None
    TextDecorationSchema.UNDERLINE -> TextDecoration.Underline
    TextDecorationSchema.LINE_THROUGH -> TextDecoration.LineThrough
}
