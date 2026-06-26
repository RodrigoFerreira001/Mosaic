package dev.catbit.mosaic.client.extensions

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.ui.unit.sp
import dev.catbit.mosaic.core.data.schemas.text.AutoSizeSchema

fun AutoSizeSchema.toTextAutoSize(): TextAutoSize = when (this) {
    is AutoSizeSchema.StepBased -> TextAutoSize.StepBased(
        minFontSize = minFontSize.sp,
        maxFontSize = maxFontSize.sp,
        stepSize = stepSize.sp
    )
}
