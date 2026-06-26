package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.style.TextOverflow
import dev.catbit.mosaic.core.data.schemas.text.TextOverflowSchema

fun TextOverflowSchema.toTextOverflow(): TextOverflow = when (this) {
    TextOverflowSchema.CLIP -> TextOverflow.Clip
    TextOverflowSchema.ELLIPSIS -> TextOverflow.Ellipsis
    TextOverflowSchema.VISIBLE -> TextOverflow.Visible
}
