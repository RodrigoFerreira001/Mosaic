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
            data class Fill(val max: Int? = null) : Horizontal

            @Serializable
            @SerialName("wrap")
            data object Wrap : Horizontal

            @Serializable
            @SerialName("fixed")
            data class Fixed(@SerialName("value") val value: Int) : Horizontal

            @Serializable
            @SerialName("weight")
            data class Weight(
                @SerialName("value") val value: Float
            ) : Horizontal

            @Serializable
            @SerialName("span")
            data class Span(
                @SerialName("value") val value: Int
            ) : Horizontal

            @Serializable
            @SerialName("flex")
            data class Flex(
                @SerialName("grow") val grow: Float?,
                @SerialName("shrink") val shrink: Float?,
                @SerialName("basis") val basis: FlexBasis?,
                @SerialName("alignSelf") val alignSelf: FlexAlignSelf?,
                @SerialName("order") val order: Int?
            ) : Horizontal {

                @Serializable
                sealed interface FlexBasis {
                    @Serializable
                    @SerialName("auto")
                    data object Auto : FlexBasis

                    @Serializable
                    @SerialName("fixed")
                    data class Fixed(
                        @SerialName("value") val value: Int
                    ) : FlexBasis

                    @Serializable
                    @SerialName("fraction")
                    data class Fraction(
                        @SerialName("value") val value: Float
                    ) : FlexBasis
                }

                @Serializable
                enum class FlexAlignSelf {
                    Auto,
                    Start,
                    Center,
                    End,
                    Stretch,
                    Baseline
                }
            }
        }

        @Serializable
        @SerialName("vertical")
        sealed interface Vertical : Behavior {

            @Serializable
            @SerialName("fill")
            data class Fill(val max: Int? = null) : Vertical

            @Serializable
            @SerialName("wrap")
            data object Wrap : Vertical

            @Serializable
            @SerialName("fixed")
            data class Fixed(
                @SerialName("value") val value: Int
            ) : Vertical

            @Serializable
            @SerialName("weight")
            data class Weight(
                @SerialName("value") val value: Float
            ) : Vertical

            @Serializable
            @SerialName("span")
            data class Span(
                @SerialName("value") val value: Int
            ) : Vertical

            @Serializable
            @SerialName("fillRow")
            data class FillRow(
                @SerialName("fraction") val fraction: Float = 1f
            ) : Vertical
        }
    }
}
