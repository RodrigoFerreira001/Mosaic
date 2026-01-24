package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.RectangleShape
import dev.catbit.mosaic.core.data.tile.style.ShapeModel

fun ShapeModel.toShape() = when(this) {
    ShapeModel.Circle -> CircleShape
    ShapeModel.Rectangle -> RectangleShape
    is ShapeModel.RoundedCornerRectangle -> radius.toShape()
}