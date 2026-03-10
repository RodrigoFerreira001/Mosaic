package dev.catbit.mosaic.server.builder.tile.builders.buttons

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class ButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val loading: Boolean
) : TileSchemaBuilder<ButtonTileSchema> {

    override fun build() = ButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        text = text,
        loading = loading
    )
}

fun TileSchemaBuilderScope.Button(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    text: String,
    loading: Boolean = false
) {
    addBuilder(
        ButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            text = text,
            loading = loading,
        )
    )
}