package dev.catbit.mosaic.core.data.schemas.tile.style

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Inner spacing applied between the tile's border/edge and its content.
 *
 * All values are in **dp**. Maps to `PaddingValues(top, end, bottom, start)` in Compose.
 * Applied as the last modifier in `styledWith()`, i.e. inside the border.
 *
 * **DSL helpers (mosaic-server) — no single-argument overload exists:**
 * ```kotlin
 * padding(horizontal = 16, vertical = 8)           // start=16, end=16, top=8, bottom=8
 * padding(horizontal = 16, top = 8, bottom = 24)   // mixed
 * padding(top = 8, end = 16, bottom = 8, start = 16) // explicit four sides
 * // padding(16) ← DOES NOT EXIST — will not compile
 * ```
 */
@Immutable
@Serializable
data class PaddingSchema(
    @SerialName("top") val top: Int,
    @SerialName("end") val end: Int,
    @SerialName("bottom") val bottom: Int,
    @SerialName("start") val start: Int
)
