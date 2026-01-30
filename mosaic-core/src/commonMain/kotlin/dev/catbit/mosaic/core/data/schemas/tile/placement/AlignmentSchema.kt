package dev.catbit.mosaic.core.data.schemas.tile.placement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AlignmentSchema {

    @Serializable
    @SerialName("Vertical")
    sealed interface Vertical : AlignmentSchema {

        @Serializable
        @SerialName("top")
        data object Top : Vertical

        @Serializable
        @SerialName("center")
        data object Center : Vertical

        @Serializable
        @SerialName("bottom")
        data object Bottom : Vertical
    }

    @Serializable
    @SerialName("Horizontal")
    sealed interface Horizontal : AlignmentSchema {

        @Serializable
        @SerialName("start")
        data object Start : Horizontal

        @Serializable
        @SerialName("center")
        data object Center : Horizontal

        @Serializable
        @SerialName("end")
        data object End : Horizontal
    }

    @Serializable
    @SerialName("TwoDimensional")
    sealed interface TwoDimensional : AlignmentSchema {

        @Serializable
        @SerialName("top_start")
        data object TopStart: TwoDimensional

        @Serializable
        @SerialName("top_center")
        data object TopCenter: TwoDimensional

        @Serializable
        @SerialName("top_end")
        data object TopEnd: TwoDimensional

        @Serializable
        @SerialName("center_start")
        data object CenterStart: TwoDimensional

        @Serializable
        @SerialName("center")
        data object Center: TwoDimensional

        @Serializable
        @SerialName("center_end")
        data object CenterEnd: TwoDimensional

        @Serializable
        @SerialName("bottom_start")
        data object BottomStart: TwoDimensional

        @Serializable
        @SerialName("bottom_center")
        data object BottomCenter: TwoDimensional

        @Serializable
        @SerialName("bottom_end")
        data object BottomEnd: TwoDimensional
    }
}