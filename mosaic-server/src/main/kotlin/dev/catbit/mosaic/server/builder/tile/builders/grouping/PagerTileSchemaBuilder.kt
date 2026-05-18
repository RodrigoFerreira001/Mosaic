package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema.PageSizeSchema
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
    private val pageSize: PageSizeSchema,
    private val pageSpacing: Int,
    private val contentPadding: Int,
    private val beyondViewportPageCount: Int
) : TileSchemaBuilder<PagerTileSchema>() {

    override fun build() = PagerTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        pageSize = pageSize,
        pageSpacing = pageSpacing,
        contentPadding = contentPadding,
        beyondViewportPageCount = beyondViewportPageCount
    )
}

fun TileSchemaBuilderScope.Pager(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    pageSize: PageSizeSchema = PageSizeSchema.Fill,
    pageSpacing: Int = 0,
    contentPadding: Int = 0,
    beyondViewportPageCount: Int = 0,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        PagerTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            visibility = visibility,
            pageSize = pageSize,
            pageSpacing = pageSpacing,
            contentPadding = contentPadding,
            beyondViewportPageCount = beyondViewportPageCount
        )
    )
}

fun pageFill(): PageSizeSchema = PageSizeSchema.Fill
fun pageFixed(value: Int): PageSizeSchema = PageSizeSchema.Fixed(value)
