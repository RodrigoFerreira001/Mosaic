package dev.catbit.mosaic.core.data.schemas.tile.style

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A single color of a gradient, optionally pinned to a position along the gradient axis.
 *
 * [stop] is a `0f..1f` fraction of the gradient axis. When **every** stop of a gradient is `null`,
 * the colors are spread evenly (matching Compose's `Brush.xGradient(colors, …)` overloads). When
 * **any** stop is provided, the client fills the remaining `null` stops with an even distribution
 * so colors and stops always have the same size.
 *
 * **DSL (mosaic-server):** gradients accept either `vararg Pair<Float, ColorSchema>`
 * (`0f to color(...)`) or a plain `List<ColorSchema>` for the evenly-spread case.
 */
@Immutable
@Serializable
data class ColorStopSchema(
    @SerialName("color") val color: ColorSchema,
    @SerialName("stop") val stop: Float? = null
)
