package dev.catbit.mosaic.server.builder.tile.builders.text

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema
import dev.catbit.mosaic.core.data.schemas.typography.TypographySchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class SimpleTextTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val color: ColorSchema?,
    private val typography: TypographySchema?
) : TileSchemaBuilder<SimpleTextTileSchema>() {

    override fun build() = SimpleTextTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        text = text,
        color = color,
        typography = typography
    )
}

fun TileSchemaBuilderScope.SimpleText(
    text: String,
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {
        size(
            width = wrapHorizontally(),
            height = wrapVertically()
        )
    },
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    color: ColorSchema? = null,
    typography: TypographySchema? = null
) {
    addBuilder(
        SimpleTextTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            text = text,
            color = color,
            typography = typography
        )
    )
}
