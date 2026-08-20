package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.schemas.tile.style.MarginSchema

/** Converts a [MarginSchema] into a Compose [PaddingValues] — used by `Modifier.styledWith` to apply
 * `style.margin`. `style.padding` is a distinct [dev.catbit.mosaic.core.data.schemas.tile.style.PaddingSchema]
 * with its own [PaddingSchema.toPaddingValues] extension, despite the identical field shape. */
fun MarginSchema.toPaddingValues() = PaddingValues(
    top = top.dp,
    end = end.dp,
    bottom = bottom.dp,
    start = start.dp,
)