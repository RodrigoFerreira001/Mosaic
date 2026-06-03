package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Clips tile content to a [ShapeSchema].
 *
 * Applied as `Modifier.clip(shape)` **before** background and border in `styledWith()`. This means
 * the background fill will be clipped to the shape, producing rounded/circular backgrounds without
 * needing a rounded border.
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * clip(circleShape())
 * clip(rectangleShape())
 * clip(roundedCornerShape(8))                                          // uniform radius
 * clip(roundedCornerShape(topStart = 8, topEnd = 8, bottomStart = 0, bottomEnd = 0))
 * clip(roundedCornerShape(radius = radius(topStart = 8, topEnd = 8, bottomStart = 0, bottomEnd = 0)))
 * ```
 */
@Serializable
data class ClipSchema(
    @SerialName("shape") val shape: ShapeSchema
)