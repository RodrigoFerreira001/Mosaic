package dev.catbit.mosaic.core.data.schemas.tile.style

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Per-corner radius in dp, used by [BorderSchema] and [ShapeSchema.RoundedCornerRectangle].
 *
 * Maps to `RoundedCornerShape(topStart, topEnd, bottomStart, bottomEnd)` in Compose.
 * All values are in **dp** and must be non-negative integers.
 *
 * **DSL helper (mosaic-server):**
 * ```kotlin
 * radius(topStart = 8, topEnd = 8, bottomStart = 8, bottomEnd = 8)   // all corners
 * radius(topStart = 16, topEnd = 16, bottomStart = 0, bottomEnd = 0) // top-only
 * ```
 *
 * **Notes:** Corner radii are layout-direction-aware (`topStart`/`topEnd` flip in RTL layouts).
 */
@Immutable
@Serializable
data class RadiusSchema(
    @SerialName("topStart") val topStart: Int,
    @SerialName("topEnd") val topEnd: Int,
    @SerialName("bottomStart") val bottomStart: Int,
    @SerialName("bottomEnd") val bottomEnd: Int
)