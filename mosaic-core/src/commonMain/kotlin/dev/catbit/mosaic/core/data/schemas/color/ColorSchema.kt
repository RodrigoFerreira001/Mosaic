package dev.catbit.mosaic.core.data.schemas.color

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ColorSchema {

    // TODO GRADIENT, BLUR?

    @Serializable
    @SerialName("hex")
    data class Hex(
        @SerialName("value") val value: String
    ) : ColorSchema

    @Serializable
    @SerialName("rgba")
    data class Rgba(
        @SerialName("r") val r: Float,
        @SerialName("g") val g: Float,
        @SerialName("b") val b: Float,
        @SerialName("alpha") val alpha: Float
    ) : ColorSchema

    @Serializable
    @SerialName("theme")
    data class Theme(
        @SerialName("value") val value: Color
    ) : ColorSchema {

        enum class Color {
            PRIMARY,
            ON_PRIMARY,
            PRIMARY_CONTAINER,
            ON_PRIMARY_CONTAINER,
            SECONDARY,
            ON_SECONDARY,
            SECONDARY_CONTAINER,
            ON_SECONDARY_CONTAINER,
            TERTIARY,
            ON_TERTIARY,
            TERTIARY_CONTAINER,
            ON_TERTIARY_CONTAINER,
            ERROR,
            ON_ERROR,
            ERROR_CONTAINER,
            ON_ERROR_CONTAINER,
            BACKGROUND,
            ON_BACKGROUND,
            SURFACE,
            ON_SURFACE,
            SURFACE_VARIANT,
            ON_SURFACE_VARIANT,
            OUTLINE,
            OUTLINE_VARIANT,
            SCRIM,
            INVERSE_SURFACE,
            INVERSE_ON_SURFACE,
            INVERSE_PRIMARY,
            SURFACE_DIM,
            SURFACE_BRIGHT,
            SURFACE_CONTAINER_LOWEST,
            SURFACE_CONTAINER_LOW,
            SURFACE_CONTAINER,
            SURFACE_CONTAINER_HIGH,
            SURFACE_CONTAINER_HIGHEST
        }
    }
}