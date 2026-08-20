package dev.catbit.mosaic.server.builder.style

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.BackgroundSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ColorStopSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.OffsetSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.TileModeSchema

/**
 * Solid-color background — the shorthand `background(color(...))` form used inside a
 * `style = { }` block also accepts this explicitly when a custom [alpha] is needed.
 *
 * @param color Fill color of the background.
 * @param alpha Opacity applied on top of [color]'s own alpha, from 0f to 1f. Defaults to 1f.
 */
fun solidColor(
    color: ColorSchema,
    alpha: Float = 1f
) = BackgroundSchema.SolidColor(
    color = color,
    alpha = alpha
)

/**
 * Linear gradient background running from [start] to [end], with explicit color stops.
 * Dimensions are in dp; `end` left `null` means the far edge of the tile.
 *
 * @param colorStops Explicit `stop to color` pairs, e.g. `0f to color(...)`.
 * @param start Starting point of the gradient line. Defaults to the tile's origin (0, 0).
 * @param end Ending point of the gradient line. Defaults to none (the tile's far edge).
 * @param tileMode How the gradient repeats past its `end` when it doesn't cover the whole tile. Defaults to clamp (extends the last color).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
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

/**
 * Linear gradient background running from [start] to [end], with [colors] spread evenly along
 * the line. Dimensions are in dp; `end` left `null` means the far edge of the tile.
 *
 * @param colors Colors spread evenly along the gradient.
 * @param start Starting point of the gradient line. Defaults to the tile's origin (0, 0).
 * @param end Ending point of the gradient line. Defaults to none (the tile's far edge).
 * @param tileMode How the gradient repeats past its `end` when it doesn't cover the whole tile. Defaults to clamp (extends the last color).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
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

/**
 * Horizontal gradient background running from [startX] to [endX], with explicit color stops.
 * Dimensions are in dp; `endX` left `null` means the tile's right edge.
 *
 * @param colorStops Explicit `stop to color` pairs, e.g. `0f to color(...)`.
 * @param startX Starting x position, in dp. Defaults to 0.
 * @param endX Ending x position, in dp. Defaults to none (the tile's right edge).
 * @param tileMode How the gradient repeats past its `endX` when it doesn't cover the whole tile. Defaults to clamp (extends the last color).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
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

/**
 * Horizontal gradient background running from [startX] to [endX], with [colors] spread evenly
 * along the line. Dimensions are in dp; `endX` left `null` means the tile's right edge.
 *
 * @param colors Colors spread evenly along the gradient.
 * @param startX Starting x position, in dp. Defaults to 0.
 * @param endX Ending x position, in dp. Defaults to none (the tile's right edge).
 * @param tileMode How the gradient repeats past its `endX` when it doesn't cover the whole tile. Defaults to clamp (extends the last color).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
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

/**
 * Vertical gradient background running from [startY] to [endY], with explicit color stops.
 * Dimensions are in dp; `endY` left `null` means the tile's bottom edge.
 *
 * @param colorStops Explicit `stop to color` pairs, e.g. `0f to color(...)`.
 * @param startY Starting y position, in dp. Defaults to 0.
 * @param endY Ending y position, in dp. Defaults to none (the tile's bottom edge).
 * @param tileMode How the gradient repeats past its `endY` when it doesn't cover the whole tile. Defaults to clamp (extends the last color).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
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

/**
 * Vertical gradient background running from [startY] to [endY], with [colors] spread evenly
 * along the line. Dimensions are in dp; `endY` left `null` means the tile's bottom edge.
 *
 * @param colors Colors spread evenly along the gradient.
 * @param startY Starting y position, in dp. Defaults to 0.
 * @param endY Ending y position, in dp. Defaults to none (the tile's bottom edge).
 * @param tileMode How the gradient repeats past its `endY` when it doesn't cover the whole tile. Defaults to clamp (extends the last color).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
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

/**
 * Radial gradient background radiating out from [center], with explicit color stops. Dimensions
 * are in dp; `center` left `null` means the tile's own center, and `radius` left `null` means
 * the largest radius that still fits the tile.
 *
 * @param colorStops Explicit `stop to color` pairs, e.g. `0f to color(...)`.
 * @param center Center point the gradient radiates from. Defaults to none (the tile's center).
 * @param radius Radius of the gradient, in dp. Defaults to none (the largest radius that fits the tile).
 * @param tileMode How the gradient repeats past its `radius` when it doesn't cover the whole tile. Defaults to clamp (extends the last color).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
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

/**
 * Radial gradient background radiating out from [center], with [colors] spread evenly along the
 * radius. Dimensions are in dp; `center` left `null` means the tile's own center, and `radius`
 * left `null` means the largest radius that still fits the tile.
 *
 * @param colors Colors spread evenly along the gradient.
 * @param center Center point the gradient radiates from. Defaults to none (the tile's center).
 * @param radius Radius of the gradient, in dp. Defaults to none (the largest radius that fits the tile).
 * @param tileMode How the gradient repeats past its `radius` when it doesn't cover the whole tile. Defaults to clamp (extends the last color).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
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

/**
 * Sweep (conic) gradient background rotating around [center], with explicit color stops.
 * `center` left `null` means the tile's own center.
 *
 * @param colorStops Explicit `stop to color` pairs, e.g. `0f to color(...)`.
 * @param center Center point the gradient sweeps around. Defaults to none (the tile's center).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
fun sweepGradient(
    vararg colorStops: Pair<Float, ColorSchema>,
    center: OffsetSchema? = null,
    alpha: Float = 1f
) = BackgroundSchema.SweepGradient(
    colorStops = colorStops.toColorStops(),
    center = center,
    alpha = alpha
)

/**
 * Sweep (conic) gradient background rotating around [center], with [colors] spread evenly
 * around the sweep. `center` left `null` means the tile's own center.
 *
 * @param colors Colors spread evenly around the gradient.
 * @param center Center point the gradient sweeps around. Defaults to none (the tile's center).
 * @param alpha Opacity applied to the whole gradient, from 0f to 1f. Defaults to 1f.
 */
fun sweepGradient(
    colors: List<ColorSchema>,
    center: OffsetSchema? = null,
    alpha: Float = 1f
) = BackgroundSchema.SweepGradient(
    colorStops = colors.toColorStops(),
    center = center,
    alpha = alpha
)

/**
 * A dp offset used to position gradients (e.g. `linearGradient`'s `start`/`end`,
 * `radialGradient`/`sweepGradient`'s `center`).
 *
 * @param x Horizontal offset, in dp. Defaults to none.
 * @param y Vertical offset, in dp. Defaults to none.
 */
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
