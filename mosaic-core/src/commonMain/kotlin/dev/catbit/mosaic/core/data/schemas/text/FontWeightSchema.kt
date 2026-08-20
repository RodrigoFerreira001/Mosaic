package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

/** `SimpleText.fontWeight` — converted to Compose's `FontWeight` via
 * `FontWeightSchema.toFontWeight()`, mirroring Compose's own 9-step weight scale. */
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
