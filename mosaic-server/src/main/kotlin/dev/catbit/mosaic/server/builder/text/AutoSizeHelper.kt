package dev.catbit.mosaic.server.builder.text

import dev.catbit.mosaic.core.data.schemas.text.AutoSizeSchema

fun stepBasedAutoSize(
    minFontSize: Float,
    maxFontSize: Float,
    stepSize: Float
) = AutoSizeSchema.StepBased(
    minFontSize = minFontSize,
    maxFontSize = maxFontSize,
    stepSize = stepSize
)
