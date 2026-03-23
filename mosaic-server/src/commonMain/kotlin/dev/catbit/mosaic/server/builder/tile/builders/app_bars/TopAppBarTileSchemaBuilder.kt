package dev.catbit.mosaic.server.builder.tile.builders.app_bars

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class TopAppBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val title: TileSchemaBuilderScope.() -> Unit,
    private val navigationIcon: (TileSchemaBuilderScope.() -> Unit)?,
    private val actions: (TileSchemaBuilderScope.() -> Unit)?,
    private val barStyle: TopAppBarTileSchema.TopAppBarStyle
) : TileSchemaBuilder<TopAppBarTileSchema> {

    override fun build() = TopAppBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        title = TileSchemaBuilderScope().apply(title).build().lastOrNull()
            ?: throw IllegalArgumentException("TopAppBar requires at least on title"),
        navigationIcon = navigationIcon?.let { TileSchemaBuilderScope().apply(it).build().lastOrNull() },
        actions = actions?.let { TileSchemaBuilderScope().apply(it).build() },
        barStyle = barStyle
    )
}

fun TileSchemaBuilderScope.TopAppBar(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    navigationIcon: (TileSchemaBuilderScope.() -> Unit)? = null,
    actions: (TileSchemaBuilderScope.() -> Unit)? = null,
    barStyle: TopAppBarTileSchema.TopAppBarStyle = TopAppBarTileSchema.TopAppBarStyle.DEFAULT,
    title: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        TopAppBarTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            barStyle = barStyle
        )
    )
}
