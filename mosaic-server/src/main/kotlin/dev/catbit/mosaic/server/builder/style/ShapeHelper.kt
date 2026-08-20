package dev.catbit.mosaic.server.builder.style

import dev.catbit.mosaic.core.data.schemas.tile.style.RadiusSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ShapeSchema

/**
 * Top-level counterparts of [StyleSchemaBuilderScope]'s shape helpers, for use outside a
 * `style = { }` block (e.g. tile parameters that take a [ShapeSchema] directly, such as
 * `Tooltip`'s `shape`).
 */

/**
 * Corner radius, in dp, individually per corner — the value fed into [roundedCornerShape] and
 * `border(...)`.
 *
 * @param topStart Radius of the top-start corner, in dp. Defaults to 0.
 * @param topEnd Radius of the top-end corner, in dp. Defaults to 0.
 * @param bottomStart Radius of the bottom-start corner, in dp. Defaults to 0.
 * @param bottomEnd Radius of the bottom-end corner, in dp. Defaults to 0.
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

/** Fully circular/oval shape — the tile is clipped to an ellipse inscribed in its bounds. */
fun circleShape() = ShapeSchema.Circle

/** Plain rectangular shape — no corner rounding, no clipping applied. */
fun rectangleShape() = ShapeSchema.Rectangle

/** Rounded-rectangle shape with a fully custom [radius] per corner. */
fun roundedCornerShape(
    radius: RadiusSchema
) = ShapeSchema.RoundedCornerRectangle(radius)

/**
 * Rounded-rectangle shape with an independent radius per corner, in dp.
 *
 * @param topStart Radius of the top-start corner, in dp.
 * @param topEnd Radius of the top-end corner, in dp.
 * @param bottomStart Radius of the bottom-start corner, in dp.
 * @param bottomEnd Radius of the bottom-end corner, in dp.
 */
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

/** Rounded-rectangle shape with the same radius, in dp, applied to all four corners. */
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
