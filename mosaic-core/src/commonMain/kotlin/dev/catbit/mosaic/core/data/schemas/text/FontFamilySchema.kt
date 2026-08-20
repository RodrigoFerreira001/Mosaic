package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

/** `SimpleText.fontFamily` — converted to Compose's `FontFamily` via
 * `FontFamilySchema.toFontFamily()`, one of the 5 generic families Compose ships (no custom/bundled
 * font support at the schema level). */
@Serializable
enum class FontFamilySchema {
    DEFAULT,
    SERIF,
    SANS_SERIF,
    MONOSPACE,
    CURSIVE
}
