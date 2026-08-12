package dev.catbit.mosaic.core.data.schemas.tile.style

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A two-dimensional position used by gradient [BackgroundSchema] variants.
 *
 * Both axes are expressed in **dp** and are relative to the top-start corner of the tile's
 * drawing area. A `null` axis maps to `Float.POSITIVE_INFINITY`, which Compose interprets as
 * "the far right ([x]) or far bottom ([y]) of the drawing area" — this is how the far edge of a
 * gradient is expressed without knowing the tile size upfront.
 *
 * **Constants:**
 * - [Zero] → `Offset.Zero`, the top-start corner.
 * - [Infinite] → `Offset.Infinite`, the bottom-end corner.
 *
 * **DSL helper (mosaic-server):**
 * ```kotlin
 * offset(x = 40, y = 40)   // 40dp, 40dp
 * offset(x = 0)            // 0dp, bottom edge
 * offset()                 // bottom-end corner
 * ```
 */
@Immutable
@Serializable
data class OffsetSchema(
    @SerialName("x") val x: Int? = null,
    @SerialName("y") val y: Int? = null
) {
    companion object {
        val Zero = OffsetSchema(x = 0, y = 0)
        val Infinite = OffsetSchema(x = null, y = null)
    }
}
