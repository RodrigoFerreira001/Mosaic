package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

/** `SimpleText.overflow` — how overflowing text is handled when it exceeds `maxLines`, converted to
 * Compose's `TextOverflow` via `TextOverflowSchema.toTextOverflow()`. */
@Serializable
enum class TextOverflowSchema {
    CLIP,
    ELLIPSIS,
    VISIBLE
}
