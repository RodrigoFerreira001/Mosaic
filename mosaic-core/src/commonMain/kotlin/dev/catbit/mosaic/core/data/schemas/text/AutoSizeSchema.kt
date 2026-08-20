package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** `SimpleText.autoSize` — automatic font-size scaling to fit the available space, converted to
 * Compose's `TextAutoSize` via `AutoSizeSchema.toTextAutoSize()`. `StepBased` is the only variant
 * today, mirroring Compose's own `TextAutoSize.StepBased`. */
@Serializable
sealed interface AutoSizeSchema {

    /**
     * Shrinks/grows the font size in discrete steps between [minFontSize] and [maxFontSize] until
     * the text fits.
     *
     * @property minFontSize smallest font size to try, in sp.
     * @property maxFontSize largest font size to try, in sp.
     * @property stepSize increment between tried sizes, in sp.
     */
    @Serializable
    @SerialName("StepBased")
    data class StepBased(
        @SerialName("minFontSize") val minFontSize: Float,
        @SerialName("maxFontSize") val maxFontSize: Float,
        @SerialName("stepSize") val stepSize: Float
    ) : AutoSizeSchema
}
