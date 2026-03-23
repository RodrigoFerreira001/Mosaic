package dev.catbit.mosaic.server.builder.tile.builders.buttons

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class FloatingActionButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val icon: IconSchema,
    private val size: FloatingActionButtonTileSchema.Size,
    private val enabled: Boolean
) : TileSchemaBuilder<FloatingActionButtonTileSchema> {

    override fun build() = FloatingActionButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        icon = icon,
        size = size,
        enabled = enabled
    )
}

fun TileSchemaBuilderScope.FloatingActionButton(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    icon: IconSchema,
    size: FloatingActionButtonTileSchema.Size = FloatingActionButtonTileSchema.Size.DEFAULT,
    enabled: Boolean
) {
    addBuilder(
        FloatingActionButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            icon = icon,
            size = size,
            enabled = enabled
        )
    )
}
