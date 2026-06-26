package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

@Serializable
enum class FontFamilySchema {
    DEFAULT,
    SERIF,
    SANS_SERIF,
    MONOSPACE,
    CURSIVE
}
