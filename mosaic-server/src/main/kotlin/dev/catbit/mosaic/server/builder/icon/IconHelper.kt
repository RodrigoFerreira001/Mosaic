package dev.catbit.mosaic.server.builder.icon

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema.Style

fun icon(
    name: String,
    color: ColorSchema? = null,
    size: Int? = 24,
    style: Style = Style.OUTLINED
) = IconSchema(
    name = name,
    color = color,
    size = size,
    style = style
)

fun outlinedIcon() = Style.OUTLINED
fun roundedIcon() = Style.ROUNDED
fun sharpIcon() = Style.SHARP

