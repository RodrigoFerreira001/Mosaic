package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class PagerTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val columns: Int,
    private val gutter: Int,
    private val contentHorizontalPadding: Int
) : TileSchemaBuilder<PagerTileSchema>() {

    override fun build() = PagerTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        columns = columns,
        gutter = gutter,
        contentHorizontalPadding = contentHorizontalPadding
    )
}

fun TileSchemaBuilderScope.Pager(
    id: String = randomUuid(),
    tiles: TileSchemaBuilderScope.() -> Unit,
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    columns: Int = 1,
    gutter: Int = 0,
    contentHorizontalPadding: Int = 0
) {
    addBuilder(
        PagerTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            visibility = visibility,
            columns = columns,
            gutter = gutter,
            contentHorizontalPadding = contentHorizontalPadding
        )
    )
}
