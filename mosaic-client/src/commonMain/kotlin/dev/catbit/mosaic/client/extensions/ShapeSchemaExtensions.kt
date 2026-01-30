package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.RectangleShape
import dev.catbit.mosaic.core.data.schemas.tile.style.ShapeSchema

fun ShapeSchema.toShape() = when(this) {
    ShapeSchema.Circle -> CircleShape
    ShapeSchema.Rectangle -> RectangleShape
    is ShapeSchema.RoundedCornerRectangle -> radius.toShape()
}