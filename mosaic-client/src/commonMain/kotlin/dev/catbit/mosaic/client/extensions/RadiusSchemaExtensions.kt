package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.schemas.tile.style.RadiusSchema

/** Converts a [RadiusSchema] into a Compose [RoundedCornerShape] — used for `style.border`'s own
 * corner radius, and (via [ShapeSchema.toShape]) for `style.clip`'s `RoundedCornerRectangle`
 * variant. */
fun RadiusSchema.toShape() = RoundedCornerShape(
    topStart = topStart.dp,
    topEnd = topEnd.dp,
    bottomStart = bottomStart.dp,
    bottomEnd = bottomEnd.dp
)