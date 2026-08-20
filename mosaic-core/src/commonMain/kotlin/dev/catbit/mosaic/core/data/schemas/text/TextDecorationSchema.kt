package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

/** `SimpleText.textDecoration` — converted to Compose's `TextDecoration` via
 * `TextDecorationSchema.toTextDecoration()`. */
@Serializable
enum class TextDecorationSchema {
    NONE,
    UNDERLINE,
    LINE_THROUGH
}
