package dev.catbit.mosaic.core.data.schemas.tile.style

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Fill applied to a tile by [StyleSchema.background]. Each variant maps 1:1 to a Compose `Brush`
 * factory and is applied through `Modifier.background(brush, alpha = alpha)`.
 *
 * **Variants:**
 * - [SolidColor] → `SolidColor` — the default, a single [ColorSchema].
 * - [LinearGradient] → `Brush.linearGradient` — arbitrary start/end points.
 * - [HorizontalGradient] → `Brush.horizontalGradient` — linear along the X axis.
 * - [VerticalGradient] → `Brush.verticalGradient` — linear along the Y axis.
 * - [RadialGradient] → `Brush.radialGradient` — circular, from a center point.
 * - [SweepGradient] → `Brush.sweepGradient` — angular, starting at 3 o'clock and going clockwise.
 *
 * **`null` means "Compose default":** since the tile size is unknown server-side, unspecified
 * geometry is sent as `null` and resolved at draw time:
 * | field | `null` resolves to |
 * |---|---|
 * | `end` (linear) | `Offset.Infinite` — bottom-end corner |
 * | `endX` / `endY` | `Float.POSITIVE_INFINITY` — far edge |
 * | `center` (radial/sweep) | `Offset.Unspecified` — center of the tile |
 * | `radius` (radial) | `Float.POSITIVE_INFINITY` — largest radius that fits |
 *
 * All dimensions are `Int` in **dp**, like every other dimension in Mosaic.
 *
 * **[alpha]** is a `0f..1f` multiplier applied to the whole fill, on top of any alpha already
 * carried by the colors themselves.
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * background(color(themeColorSurface()))                       // shorthand for SolidColor
 * background(solidColor(color(themeColorSurface()), alpha = 0.5f))
 * background(verticalGradient(0f to color(...), 1f to color(...)))
 * background(linearGradient(listOf(color(...), color(...))))   // evenly spread
 * background(radialGradient(listOf(color(...), color(...)), center = offset(40, 40), radius = 80))
 * background(sweepGradient(listOf(color(...), color(...), color(...))))
 * ```
 *
 * **Note:** blur is not supported yet.
 */
@Immutable
@Serializable
sealed interface BackgroundSchema {

    val alpha: Float

    @Immutable
    @Serializable
    @SerialName("solid_color")
    data class SolidColor(
        @SerialName("color") val color: ColorSchema,
        @SerialName("alpha") override val alpha: Float = 1f
    ) : BackgroundSchema

    @Immutable
    @Serializable
    @SerialName("linear_gradient")
    data class LinearGradient(
        @SerialName("colorStops") val colorStops: List<ColorStopSchema>,
        @SerialName("start") val start: OffsetSchema = OffsetSchema.Zero,
        @SerialName("end") val end: OffsetSchema? = null,
        @SerialName("tileMode") val tileMode: TileModeSchema = TileModeSchema.CLAMP,
        @SerialName("alpha") override val alpha: Float = 1f
    ) : BackgroundSchema

    @Immutable
    @Serializable
    @SerialName("horizontal_gradient")
    data class HorizontalGradient(
        @SerialName("colorStops") val colorStops: List<ColorStopSchema>,
        @SerialName("startX") val startX: Int = 0,
        @SerialName("endX") val endX: Int? = null,
        @SerialName("tileMode") val tileMode: TileModeSchema = TileModeSchema.CLAMP,
        @SerialName("alpha") override val alpha: Float = 1f
    ) : BackgroundSchema

    @Immutable
    @Serializable
    @SerialName("vertical_gradient")
    data class VerticalGradient(
        @SerialName("colorStops") val colorStops: List<ColorStopSchema>,
        @SerialName("startY") val startY: Int = 0,
        @SerialName("endY") val endY: Int? = null,
        @SerialName("tileMode") val tileMode: TileModeSchema = TileModeSchema.CLAMP,
        @SerialName("alpha") override val alpha: Float = 1f
    ) : BackgroundSchema

    @Immutable
    @Serializable
    @SerialName("radial_gradient")
    data class RadialGradient(
        @SerialName("colorStops") val colorStops: List<ColorStopSchema>,
        @SerialName("center") val center: OffsetSchema? = null,
        @SerialName("radius") val radius: Int? = null,
        @SerialName("tileMode") val tileMode: TileModeSchema = TileModeSchema.CLAMP,
        @SerialName("alpha") override val alpha: Float = 1f
    ) : BackgroundSchema

    @Immutable
    @Serializable
    @SerialName("sweep_gradient")
    data class SweepGradient(
        @SerialName("colorStops") val colorStops: List<ColorStopSchema>,
        @SerialName("center") val center: OffsetSchema? = null,
        @SerialName("alpha") override val alpha: Float = 1f
    ) : BackgroundSchema
}
