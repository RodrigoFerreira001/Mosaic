package dev.catbit.mosaic.server.builder.tile.builders.app_bars

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.BottomAppBarTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class BottomAppBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val actions: TileSchemaBuilderScope.() -> Unit,
    private val floatingActionButton: (TileSchemaBuilderScope.() -> Unit)?
) : TileSchemaBuilder<BottomAppBarTileSchema> {

    override fun build() = BottomAppBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        actions = TileSchemaBuilderScope().apply(actions).build(),
        floatingActionButton = floatingActionButton?.let { TileSchemaBuilderScope().apply(it).build().lastOrNull() }
    )
}

fun TileSchemaBuilderScope.BottomAppBar(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    floatingActionButton: (TileSchemaBuilderScope.() -> Unit)? = null,
    actions: TileSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        BottomAppBarTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            actions = actions,
            floatingActionButton = floatingActionButton
        )
    )
}
