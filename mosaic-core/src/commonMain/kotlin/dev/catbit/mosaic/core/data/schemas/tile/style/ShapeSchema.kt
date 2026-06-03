package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Discriminated union of Compose shapes used for [ClipSchema] and [BorderSchema.radius].
 *
 * **Variants:**
 * - [Circle] → `CircleShape` (fully circular, useful for avatar tiles or FABs).
 * - [Rectangle] → `RectangleShape` (sharp corners, default when no clip is set).
 * - [RoundedCornerRectangle] → `RoundedCornerShape` with per-corner dp radii via [RadiusSchema].
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * circleShape()
 * rectangleShape()
 * roundedCornerShape(8)                                                  // uniform Int shorthand
 * roundedCornerShape(topStart = 8, topEnd = 8, bottomStart = 0, bottomEnd = 0)
 * roundedCornerShape(radius = radius(topStart = 8, topEnd = 8, bottomStart = 0, bottomEnd = 0))
 * ```
 */
@Serializable
sealed interface ShapeSchema {

    @Serializable
    @SerialName("circle")
    data object Circle : ShapeSchema

    @Serializable
    @SerialName("rectangle")
    data object Rectangle : ShapeSchema

    @Serializable
    @SerialName("rounded_corner_rectangle")
    data class RoundedCornerRectangle(
        @SerialName("radius") val radius: RadiusSchema
    ) : ShapeSchema
}