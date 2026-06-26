package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

@Serializable
enum class FontWeightSchema {
    THIN,
    EXTRA_LIGHT,
    LIGHT,
    NORMAL,
    MEDIUM,
    SEMI_BOLD,
    BOLD,
    EXTRA_BOLD,
    BLACK
}
