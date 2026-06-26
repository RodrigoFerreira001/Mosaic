package dev.catbit.mosaic.core.data.schemas.text

import kotlinx.serialization.Serializable

@Serializable
enum class TextAlignSchema {
    LEFT,
    RIGHT,
    CENTER,
    JUSTIFY,
    START,
    END
}
