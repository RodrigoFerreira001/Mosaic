package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.AutoSizeSchema

/**
 * Automatic font-size scaling for `SimpleText`: the renderer picks the largest size, stepping
 * down from [maxFontSize] to [minFontSize] by [stepSize], that still lets the text fit its
 * available space.
 *
 * @param minFontSize Smallest font size tried, in sp.
 * @param maxFontSize Largest font size tried, in sp.
 * @param stepSize Decrement applied between each size tried, in sp.
 */
fun stepBasedAutoSize(
    minFontSize: Float,
    maxFontSize: Float,
    stepSize: Float
) = AutoSizeSchema.StepBased(
    minFontSize = minFontSize,
    maxFontSize = maxFontSize,
    stepSize = stepSize
)
