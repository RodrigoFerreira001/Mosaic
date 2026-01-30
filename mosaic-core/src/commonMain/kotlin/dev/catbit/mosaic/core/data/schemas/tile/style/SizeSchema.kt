package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SizeSchema(
    @SerialName("width") val width: Behavior.Horizontal,
    @SerialName("height") val height: Behavior.Vertical
) {
    @Serializable
    @SerialName("behavior")
    sealed interface Behavior {

        @Serializable
        @SerialName("horizontal")
        sealed interface Horizontal : Behavior {

            @Serializable
            @SerialName("fill")
            data object Fill : Horizontal

            @Serializable
            @SerialName("wrap")
            data object Wrap : Horizontal

            @Serializable
            @SerialName("fixed")
            data class Fixed(
                @SerialName("value") val value: Int
            ) : Horizontal

            @Serializable
            @SerialName("weight")
            data class Weight(
                @SerialName("value") val value: Float
            ) : Horizontal

            @Serializable
            @SerialName("span")
            data class Span(
                @SerialName("") val value: Int
            ) : Horizontal
        }

        @Serializable
        @SerialName("vertical")
        sealed interface Vertical : Behavior {

            @Serializable
            @SerialName("fill")
            data object Fill : Vertical

            @Serializable
            @SerialName("wrap")
            data object Wrap : Vertical

            @Serializable
            @SerialName("weight")
            data class Weight(
                @SerialName("value") val value: Float
            ) : Vertical

            @Serializable
            @SerialName("fixed")
            data class Fixed(
                @SerialName("value") val value: Int
            ) : Vertical
        }
    }
}