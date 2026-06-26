package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.style.TextDecoration
import dev.catbit.mosaic.core.data.schemas.text.TextDecorationSchema

fun TextDecorationSchema.toTextDecoration(): TextDecoration = when (this) {
    TextDecorationSchema.NONE -> TextDecoration.None
    TextDecorationSchema.UNDERLINE -> TextDecoration.Underline
    TextDecorationSchema.LINE_THROUGH -> TextDecoration.LineThrough
}
