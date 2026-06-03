package dev.catbit.mosaic.core.data.schemas.tile.style

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Draws a border around the tile, applied over the background in `styledWith()`.
 *
 * - [color] — border color (any [ColorSchema] variant).
 * - [thickness] — border width in dp.
 * - [radius] — optional corner radius as [RadiusSchema]. When `null`, the border uses
 *   `RectangleShape` (sharp corners). To get rounded borders, provide a [RadiusSchema];
 *   this is independent of [ClipSchema] — both can coexist.
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * border(color = color(themeColorOutline()), thickness = 1)
 * border(
 *     color = color(themeColorOutline()),
 *     thickness = 1,
 *     radius = radius(topStart = 8, topEnd = 8, bottomStart = 8, bottomEnd = 8)
 * )
 * // border(color = ..., thickness = 1, radius = 8) ← WRONG — radius must be RadiusSchema, not Int
 * ```
 */
@Serializable
data class BorderSchema(
    @SerialName("color") val color: ColorSchema,
    @SerialName("thickness") val thickness: Int,
    @SerialName("radius") val radius: RadiusSchema?
)