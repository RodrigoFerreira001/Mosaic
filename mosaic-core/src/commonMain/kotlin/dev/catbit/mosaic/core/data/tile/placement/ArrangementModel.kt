package dev.catbit.mosaic.core.data.tile.placement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ArrangementModel {

    @Serializable
    @SerialName("Horizontal")
    sealed interface Horizontal : ArrangementModel {

        @Serializable
        @SerialName("start")
        data object Start : Horizontal

        @Serializable
        @SerialName("end")
        data object End : Horizontal

        @Serializable
        @SerialName("spaced_by")
        data class SpacedBy(
            val space: Int,
            val alignment: AlignmentModel.Horizontal
        ) : Horizontal
    }

    @Serializable
    @SerialName("Vertical")
    sealed interface Vertical : ArrangementModel {

        @Serializable
        @SerialName("top")
        data object Top : Vertical

        @Serializable
        @SerialName("bottom")
        data object Bottom : Vertical

        @Serializable
        @SerialName("spaced_by")
        data class SpacedBy(
            val space: Int,
            val alignment: AlignmentModel.Vertical
        ) : Vertical
    }

    @Serializable
    @SerialName("HorizontalOrVertical")
    sealed interface HorizontalOrVertical : Horizontal, Vertical {

        @Serializable
        @SerialName("center")
        data object Center : HorizontalOrVertical

        @Serializable
        @SerialName("space_evenly")
        data object SpaceEvenly : HorizontalOrVertical

        @Serializable
        @SerialName("space_between")
        data object SpaceBetween : HorizontalOrVertical

        @Serializable
        @SerialName("space_around")
        data object SpaceAround : HorizontalOrVertical
    }
}