package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

@Serializable
enum class TextOverflowSchema {
    CLIP,
    ELLIPSIS,
    VISIBLE
}
