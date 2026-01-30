package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.schemas.tile.style.RadiusSchema

fun RadiusSchema.toShape() = RoundedCornerShape(
    topStart = topStart.dp,
    topEnd = topEnd.dp,
    bottomStart = bottomStart.dp,
    bottomEnd = bottomEnd.dp
)