package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

@Serializable
enum class TextDecorationSchema {
    NONE,
    UNDERLINE,
    LINE_THROUGH
}
