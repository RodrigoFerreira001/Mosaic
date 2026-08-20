package dev.catbit.mosaic.server.builder.icon

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema.Style

/**
 * Builds an [IconSchema] describing a single Material Symbol, used anywhere a tile takes an icon
 * (buttons, chips, text fields, nav items, etc).
 *
 * @param name Material Symbol name looked up on the client (e.g. `"settings"`, `"delete"`).
 * @param color Tint applied to the icon. Defaults to none (inherits the surrounding content color).
 * @param size Size of the icon, in dp. Defaults to 24.
 * @param style Visual style of the glyph — [outlinedIcon], [roundedIcon] or [sharpIcon]. Defaults to outlined.
 */
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

/** Outlined Material Symbol style — open, line-based glyph. */
fun outlinedIcon() = Style.OUTLINED

/** Rounded Material Symbol style — softened, curved glyph. */
fun roundedIcon() = Style.ROUNDED

/** Sharp Material Symbol style — angular, hard-edged glyph. */
fun sharpIcon() = Style.SHARP

