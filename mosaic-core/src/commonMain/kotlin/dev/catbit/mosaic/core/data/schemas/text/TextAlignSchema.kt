package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

/** `SimpleText.textAlign` — converted to Compose's `TextAlign` via
 * `TextAlignSchema.toTextAlign()`. `LEFT`/`RIGHT` are absolute (ignore layout direction); `START`/
 * `END` follow the current `LayoutDirection`. */
@Serializable
enum class TextAlignSchema {
    LEFT,
    RIGHT,
    CENTER,
    JUSTIFY,
    START,
    END
}
