package dev.catbit.mosaic.client.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.core.data.schemas.tile.style.BackgroundSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.ColorStopSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.OffsetSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.TileModeSchema
import androidx.compose.ui.graphics.SolidColor as ComposeSolidColor

/**
 * Converts a [BackgroundSchema] into the Compose [Brush] that renders it.
 *
 * The brush is remembered against the schema, the resolved colors and the current density, so it
 * is only rebuilt when one of those actually changes. Sizing is intentionally left to the brush
 * itself — `ShaderBrush.createShader(size)` resolves `Offset.Infinite`, `Offset.Unspecified` and
 * `Float.POSITIVE_INFINITY` at draw time, which is why the schema never needs the tile size.
 */
@Composable
fun BackgroundSchema.toBrush(): Brush {
    val density = LocalDensity.current

    return when (this) {
        is BackgroundSchema.SolidColor -> {
            val color = color.toComposeColor()
            remember(color) { ComposeSolidColor(color) }
        }

        is BackgroundSchema.LinearGradient -> {
            val colors = colorStops.toComposeColors()
            remember(this, colors, density) {
                gradient(
                    colors = colors,
                    stops = colorStops.toStops(),
                    fromColors = {
                        Brush.linearGradient(
                            colors = it,
                            start = start.toOffset(density, Offset.Zero),
                            end = end.toOffset(density, Offset.Infinite),
                            tileMode = tileMode.toTileMode()
                        )
                    },
                    fromColorStops = {
                        Brush.linearGradient(
                            colorStops = it,
                            start = start.toOffset(density, Offset.Zero),
                            end = end.toOffset(density, Offset.Infinite),
                            tileMode = tileMode.toTileMode()
                        )
                    }
                )
            }
        }

        is BackgroundSchema.HorizontalGradient -> {
            val colors = colorStops.toComposeColors()
            remember(this, colors, density) {
                gradient(
                    colors = colors,
                    stops = colorStops.toStops(),
                    fromColors = {
                        Brush.horizontalGradient(
                            colors = it,
                            startX = startX.toPx(density),
                            endX = endX.toPxOrInfinity(density),
                            tileMode = tileMode.toTileMode()
                        )
                    },
                    fromColorStops = {
                        Brush.horizontalGradient(
                            colorStops = it,
                            startX = startX.toPx(density),
                            endX = endX.toPxOrInfinity(density),
                            tileMode = tileMode.toTileMode()
                        )
                    }
                )
            }
        }

        is BackgroundSchema.VerticalGradient -> {
            val colors = colorStops.toComposeColors()
            remember(this, colors, density) {
                gradient(
                    colors = colors,
                    stops = colorStops.toStops(),
                    fromColors = {
                        Brush.verticalGradient(
                            colors = it,
                            startY = startY.toPx(density),
                            endY = endY.toPxOrInfinity(density),
                            tileMode = tileMode.toTileMode()
                        )
                    },
                    fromColorStops = {
                        Brush.verticalGradient(
                            colorStops = it,
                            startY = startY.toPx(density),
                            endY = endY.toPxOrInfinity(density),
                            tileMode = tileMode.toTileMode()
                        )
                    }
                )
            }
        }

        is BackgroundSchema.RadialGradient -> {
            val colors = colorStops.toComposeColors()
            remember(this, colors, density) {
                gradient(
                    colors = colors,
                    stops = colorStops.toStops(),
                    fromColors = {
                        Brush.radialGradient(
                            colors = it,
                            center = center.toOffset(density, Offset.Unspecified),
                            radius = radius.toPxOrInfinity(density),
                            tileMode = tileMode.toTileMode()
                        )
                    },
                    fromColorStops = {
                        Brush.radialGradient(
                            colorStops = it,
                            center = center.toOffset(density, Offset.Unspecified),
                            radius = radius.toPxOrInfinity(density),
                            tileMode = tileMode.toTileMode()
                        )
                    }
                )
            }
        }

        is BackgroundSchema.SweepGradient -> {
            val colors = colorStops.toComposeColors()
            remember(this, colors, density) {
                gradient(
                    colors = colors,
                    stops = colorStops.toStops(),
                    fromColors = {
                        Brush.sweepGradient(
                            colors = it,
                            center = center.toOffset(density, Offset.Unspecified)
                        )
                    },
                    fromColorStops = {
                        Brush.sweepGradient(
                            colorStops = it,
                            center = center.toOffset(density, Offset.Unspecified)
                        )
                    }
                )
            }
        }
    }
}

/**
 * Picks the right `Brush` factory overload: the evenly-spread one when no stop was specified,
 * or the color-stop one otherwise.
 */
private inline fun gradient(
    colors: List<Color>,
    stops: List<Float>?,
    fromColors: (List<Color>) -> Brush,
    fromColorStops: (Array<Pair<Float, Color>>) -> Brush
): Brush = if (stops == null) {
    fromColors(colors)
} else {
    fromColorStops(Array(colors.size) { index -> stops[index] to colors[index] })
}

/** Resolves every stop's [ColorStopSchema.color] to a Compose [Color], preserving order. */
@Composable
private fun List<ColorStopSchema>.toComposeColors(): List<Color> = map { it.color.toComposeColor() }

/**
 * `null` when every stop is unspecified — meaning the colors should be spread evenly. Otherwise
 * every unspecified stop is filled with its even-distribution position, so stops and colors always
 * have the same size.
 */
private fun List<ColorStopSchema>.toStops(): List<Float>? {
    if (all { it.stop == null }) return null
    val lastIndex = (size - 1).coerceAtLeast(1)
    return List(size) { index -> this[index].stop ?: (index.toFloat() / lastIndex) }
}

/** Converts an [OffsetSchema] to a Compose [Offset] in px, or [default] when the schema is `null` —
 * used to resolve a gradient's `start`/`end`/`center`, each optional on the wire. */
private fun OffsetSchema?.toOffset(
    density: Density,
    default: Offset
): Offset = if (this == null) {
    default
} else {
    Offset(x.toPxOrInfinity(density), y.toPxOrInfinity(density))
}

/** Converts this dp value to px under [density]. */
private fun Int.toPx(density: Density): Float = with(density) { this@toPx.dp.toPx() }

/** Same as [toPx], but resolves a `null` receiver to `Float.POSITIVE_INFINITY` — used for a
 * gradient's optional `endX`/`endY`/`radius`, where omitted means "extend to infinity". */
private fun Int?.toPxOrInfinity(
    density: Density
): Float = this?.toPx(density) ?: Float.POSITIVE_INFINITY

/** Converts the wire-format [TileModeSchema] into its Compose [TileMode] counterpart — how a
 * gradient repeats past its declared extent. */
private fun TileModeSchema.toTileMode(): TileMode = when (this) {
    TileModeSchema.CLAMP -> TileMode.Clamp
    TileModeSchema.REPEATED -> TileMode.Repeated
    TileModeSchema.MIRROR -> TileMode.Mirror
    TileModeSchema.DECAL -> TileMode.Decal
}
