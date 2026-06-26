package dev.catbit.mosaic.client.extensions

import androidx.compose.ui.text.font.FontWeight
import dev.catbit.mosaic.core.data.schemas.text.FontWeightSchema

fun FontWeightSchema.toFontWeight(): FontWeight = when (this) {
    FontWeightSchema.THIN -> FontWeight.Thin
    FontWeightSchema.EXTRA_LIGHT -> FontWeight.ExtraLight
    FontWeightSchema.LIGHT -> FontWeight.Light
    FontWeightSchema.NORMAL -> FontWeight.Normal
    FontWeightSchema.MEDIUM -> FontWeight.Medium
    FontWeightSchema.SEMI_BOLD -> FontWeight.SemiBold
    FontWeightSchema.BOLD -> FontWeight.Bold
    FontWeightSchema.EXTRA_BOLD -> FontWeight.ExtraBold
    FontWeightSchema.BLACK -> FontWeight.Black
}
