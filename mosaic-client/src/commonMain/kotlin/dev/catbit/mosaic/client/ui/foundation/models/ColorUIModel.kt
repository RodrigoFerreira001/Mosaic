package dev.catbit.mosaic.client.ui.foundation.models

sealed interface ColorUIModel {

    data class Hex(
        val value: String
    ) : ColorUIModel

    data class Rgba(
        val r: Float,
        val g: Float,
        val b: Float,
        val alpha: Float
    ) : ColorUIModel

    data class Theme(
        val value: Color
    ) : ColorUIModel {

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