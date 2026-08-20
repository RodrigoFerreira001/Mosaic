package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.schemas.tile.style.PaddingSchema

/** Converts a [PaddingSchema] into a Compose [PaddingValues] — used by `Modifier.styledWith` to apply
 * `style.padding`. `style.margin` is a distinct
 * [dev.catbit.mosaic.core.data.schemas.tile.style.MarginSchema] with its own
 * [MarginSchema.toPaddingValues] extension, despite the identical field shape. */
fun PaddingSchema.toPaddingValues() = PaddingValues(
    top = top.dp,
    end = end.dp,
    bottom = bottom.dp,
    start = start.dp,
)