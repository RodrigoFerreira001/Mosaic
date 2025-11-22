package dev.catbit.mosaic.core.data.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ShapeModel {

    @Serializable
    @SerialName("circle")
    data object Circle : ShapeModel

    @Serializable
    @SerialName("rectangle")
    data object Rectangle : ShapeModel

    @Serializable
    @SerialName("rounded_corner_rectangle")
    data class RoundedCornerRectangle(
        @SerialName("radius") val radius: RadiusModel
    ) : ShapeModel
}