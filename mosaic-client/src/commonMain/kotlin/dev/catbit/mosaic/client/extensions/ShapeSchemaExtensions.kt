package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.RectangleShape
import dev.catbit.mosaic.core.data.schemas.tile.style.ShapeSchema

/** Converts a [ShapeSchema] into its Compose `Shape` counterpart — used by `Modifier.styledWith` to
 * apply `style.clip`. */
fun ShapeSchema.toShape() = when(this) {
    ShapeSchema.Circle -> CircleShape
    ShapeSchema.Rectangle -> RectangleShape
    is ShapeSchema.RoundedCornerRectangle -> radius.toShape()
}