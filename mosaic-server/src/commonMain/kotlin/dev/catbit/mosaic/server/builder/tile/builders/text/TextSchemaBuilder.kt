package dev.catbit.mosaic.server.builder.tile.builders.text

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.TextTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class TextSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    private val visibility: TileSchema.Visibility,
    private val text: String
) : TileSchemaBuilder<TextTileSchema> {

    override fun build() = TextTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        text = text
    )
}

fun TileSchemaBuilderScope.Text(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    text: String
) {
    addBuilder(
        TextSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            text = text
        )
    )
}
