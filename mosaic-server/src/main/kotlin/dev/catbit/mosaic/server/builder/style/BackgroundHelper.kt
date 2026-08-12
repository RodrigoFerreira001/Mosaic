package dev.catbit.mosaic.server.builder.style

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.BackgroundSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ColorStopSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.OffsetSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.TileModeSchema

/**
 * Helpers that build a [BackgroundSchema] for `background(...)` inside a `style = { }` block.
 *
 * Each gradient comes in two flavours, mirroring Compose's `Brush` factories:
 * - `vararg Pair<Float, ColorSchema>` — explicit stops, e.g. `0f to color(...)`;
 * - `List<ColorSchema>` — colors spread evenly along the gradient.
 *
 * Dimensions are in **dp**. `null` means "Compose default": the far edge for `end`/`endX`/`endY`,
 * the tile center for `center`, and the largest fitting radius for `radius`.
 *
 * ```kotlin
 * style = {
 *     clip(roundedCornerShape(16))
 *     background(
 *         verticalGradient(
 *             0f to color(themeColorPrimary()),
 *             1f to color(themeColorTertiary())
 *         )
 *     )
 * }
 * ```
 */
fun solidColor(
    color: ColorSchema,
    alpha: Float = 1f
) = BackgroundSchema.SolidColor(
    color = color,
    alpha = alpha
)

fun linearGradient(
    vararg colorStops: Pair<Float, ColorSchema>,
    start: OffsetSchema = OffsetSchema.Zero,
    end: OffsetSchema? = null,
    tileMode: TileModeSchema = TileModeSchema.CLAMP,
    alpha: Float = 1f
) = BackgroundSchema.LinearGradient(
    colorStops = colorStops.toColorStops(),
    start = start,
    end = end,
    tileMode = tileMode,
    alpha = alpha
)

fun linearGradient(
    colors: List<ColorSchema>,
    start: OffsetSchema = OffsetSchema.Zero,
    end: OffsetSchema? = null,
    tileMode: TileModeSchema = TileModeSchema.CLAMP,
    alpha: Float = 1f
) = BackgroundSchema.LinearGradient(
    colorStops = colors.toColorStops(),
    start = start,
    end = end,
    tileMode = tileMode,
    alpha = alpha
)

fun horizontalGradient(
    vararg colorStops: Pair<Float, ColorSchema>,
    startX: Int = 0,
    endX: Int? = null,
    tileMode: TileModeSchema = TileModeSchema.CLAMP,
    alpha: Float = 1f
) = BackgroundSchema.HorizontalGradient(
    colorStops = colorStops.toColorStops(),
    startX = startX,
    endX = endX,
    tileMode = tileMode,
    alpha = alpha
)

fun horizontalGradient(
    colors: List<ColorSchema>,
    startX: Int = 0,
    endX: Int? = null,
    tileMode: TileModeSchema = TileModeSchema.CLAMP,
    alpha: Float = 1f
) = BackgroundSchema.HorizontalGradient(
    colorStops = colors.toColorStops(),
    startX = startX,
    endX = endX,
    tileMode = tileMode,
    alpha = alpha
)

fun verticalGradient(
    vararg colorStops: Pair<Float, ColorSchema>,
    startY: Int = 0,
    endY: Int? = null,
    tileMode: TileModeSchema = TileModeSchema.CLAMP,
    alpha: Float = 1f
) = BackgroundSchema.VerticalGradient(
    colorStops = colorStops.toColorStops(),
    startY = startY,
    endY = endY,
    tileMode = tileMode,
    alpha = alpha
)

fun verticalGradient(
    colors: List<ColorSchema>,
    startY: Int = 0,
    endY: Int? = null,
    tileMode: TileModeSchema = TileModeSchema.CLAMP,
    alpha: Float = 1f
) = BackgroundSchema.VerticalGradient(
    colorStops = colors.toColorStops(),
    startY = startY,
    endY = endY,
    tileMode = tileMode,
    alpha = alpha
)

fun radialGradient(
    vararg colorStops: Pair<Float, ColorSchema>,
    center: OffsetSchema? = null,
    radius: Int? = null,
    tileMode: TileModeSchema = TileModeSchema.CLAMP,
    alpha: Float = 1f
) = BackgroundSchema.RadialGradient(
    colorStops = colorStops.toColorStops(),
    center = center,
    radius = radius,
    tileMode = tileMode,
    alpha = alpha
)

fun radialGradient(
    colors: List<ColorSchema>,
    center: OffsetSchema? = null,
    radius: Int? = null,
    tileMode: TileModeSchema = TileModeSchema.CLAMP,
    alpha: Float = 1f
) = BackgroundSchema.RadialGradient(
    colorStops = colors.toColorStops(),
    center = center,
    radius = radius,
    tileMode = tileMode,
    alpha = alpha
)

fun sweepGradient(
    vararg colorStops: Pair<Float, ColorSchema>,
    center: OffsetSchema? = null,
    alpha: Float = 1f
) = BackgroundSchema.SweepGradient(
    colorStops = colorStops.toColorStops(),
    center = center,
    alpha = alpha
)

fun sweepGradient(
    colors: List<ColorSchema>,
    center: OffsetSchema? = null,
    alpha: Float = 1f
) = BackgroundSchema.SweepGradient(
    colorStops = colors.toColorStops(),
    center = center,
    alpha = alpha
)

fun offset(
    x: Int? = null,
    y: Int? = null
) = OffsetSchema(
    x = x,
    y = y
)

private fun Array<out Pair<Float, ColorSchema>>.toColorStops() = map { (stop, color) ->
    ColorStopSchema(
        color = color,
        stop = stop
    )
}

private fun List<ColorSchema>.toColorStops() = map { color ->
    ColorStopSchema(
        color = color
    )
}
