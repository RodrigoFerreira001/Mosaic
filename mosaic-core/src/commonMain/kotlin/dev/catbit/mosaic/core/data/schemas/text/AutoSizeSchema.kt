package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AutoSizeSchema {

    @Serializable
    @SerialName("StepBased")
    data class StepBased(
        @SerialName("minFontSize") val minFontSize: Float,
        @SerialName("maxFontSize") val maxFontSize: Float,
        @SerialName("stepSize") val stepSize: Float
    ) : AutoSizeSchema
}
