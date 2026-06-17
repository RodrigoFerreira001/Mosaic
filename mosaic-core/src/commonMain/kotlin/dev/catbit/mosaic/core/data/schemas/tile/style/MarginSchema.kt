package dev.catbit.mosaic.core.data.schemas.tile.style

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Outer spacing around the tile, applied before size constraints.
 *
 * All values are in **dp**. Maps to `PaddingValues(top, end, bottom, start)` and applied via
 * `Modifier.padding` as the second modifier in `styledWith()` (after windowInsets, before size).
 *
 * **DSL helpers (mosaic-server) — no single-argument overload exists:**
 * ```kotlin
 * margin(horizontal = 24, vertical = 0)
 * margin(horizontal = 24, top = 0, bottom = 8)
 * margin(top = 8, end = 0, bottom = 16, start = 0)
 * // margin(16) ← DOES NOT EXIST — will not compile
 * ```
 */
@Immutable
@Serializable
data class MarginSchema(
    @SerialName("top") val top: Int,
    @SerialName("end") val end: Int,
    @SerialName("bottom") val bottom: Int,
    @SerialName("start") val start: Int
)
