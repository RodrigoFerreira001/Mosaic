package dev.catbit.mosaic.core.data.schemas.tile.placement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ArrangementSchema {

    @Serializable
    @SerialName("Horizontal")
    sealed interface Horizontal : ArrangementSchema {

        @Serializable
        @SerialName("Start")
        data object Start : Horizontal

        @Serializable
        @SerialName("End")
        data object End : Horizontal

        @Serializable
        @SerialName("SpacedBy")
        data class SpacedBy(
            val space: Int,
            val alignment: AlignmentSchema.Horizontal
        ) : Horizontal
    }

    @Serializable
    @SerialName("Vertical")
    sealed interface Vertical : ArrangementSchema {

        @Serializable
        @SerialName("Top")
        data object Top : Vertical

        @Serializable
        @SerialName("Bottom")
        data object Bottom : Vertical

        @Serializable
        @SerialName("SpacedBy")
        data class SpacedBy(
            val space: Int,
            val alignment: AlignmentSchema.Vertical
        ) : Vertical
    }

    @Serializable
    @SerialName("HorizontalOrVertical")
    sealed interface HorizontalOrVertical : Horizontal, Vertical {

        @Serializable
        @SerialName("Center")
        data object Center : HorizontalOrVertical

        @Serializable
        @SerialName("SpaceEvenly")
        data object SpaceEvenly : HorizontalOrVertical

        @Serializable
        @SerialName("SpaceBetween")
        data object SpaceBetween : HorizontalOrVertical

        @Serializable
        @SerialName("SpaceAround")
        data object SpaceAround : HorizontalOrVertical
    }
}