package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

/** `SimpleText.fontStyle` — converted to Compose's `FontStyle` via
 * `FontStyleSchema.toFontStyle()`. */
@Serializable
enum class FontStyleSchema {
    NORMAL,
    ITALIC
}
