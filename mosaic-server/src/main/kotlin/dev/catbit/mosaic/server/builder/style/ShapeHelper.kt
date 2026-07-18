package dev.catbit.mosaic.server.builder.style

import dev.catbit.mosaic.core.data.schemas.tile.style.RadiusSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ShapeSchema

/**
 * Top-level counterparts of [StyleSchemaBuilderScope]'s shape helpers, for use outside a
 * `style = { }` block (e.g. tile parameters that take a [ShapeSchema] directly).
 */
fun radius(
    topStart: Int = 0,
    topEnd: Int = 0,
    bottomStart: Int = 0,
    bottomEnd: Int = 0
) = RadiusSchema(
    topStart = topStart,
    topEnd = topEnd,
    bottomStart = bottomStart,
    bottomEnd = bottomEnd
)

fun circleShape() = ShapeSchema.Circle

fun rectangleShape() = ShapeSchema.Rectangle

fun roundedCornerShape(
    radius: RadiusSchema
) = ShapeSchema.RoundedCornerRectangle(radius)

fun roundedCornerShape(
    topStart: Int,
    topEnd: Int,
    bottomStart: Int,
    bottomEnd: Int
) = ShapeSchema.RoundedCornerRectangle(
    RadiusSchema(
        topStart = topStart,
        topEnd = topEnd,
        bottomStart = bottomStart,
        bottomEnd = bottomEnd
    )
)

fun roundedCornerShape(
    all: Int
) = ShapeSchema.RoundedCornerRectangle(
    RadiusSchema(
        topStart = all,
        topEnd = all,
        bottomStart = all,
        bottomEnd = all
    )
)
