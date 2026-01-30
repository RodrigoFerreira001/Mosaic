package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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