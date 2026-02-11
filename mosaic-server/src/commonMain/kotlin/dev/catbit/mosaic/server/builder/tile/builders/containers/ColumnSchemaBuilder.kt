package dev.catbit.mosaic.server.builder.tile.builders.containers

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.ColumnTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToStart
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallyToTop
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class ColumnSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    private val visibility: TileSchema.Visibility,
    private val arrangement: ArrangementSchema.Vertical,
    private val alignment: AlignmentSchema.Horizontal,
    private val isScrollable: Boolean,
    private val lazyRender: Boolean
) : TileSchemaBuilder<ColumnTileSchema> {

    override fun build() = ColumnTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        arrangement = arrangement,
        alignment = alignment,
        isScrollable = isScrollable,
        lazyRender = lazyRender
    )
}

fun TileSchemaBuilderScope.Column(
    id: String = randomUuid(),
    tiles: TileSchemaBuilderScope.() -> Unit,
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    arrangement: ArrangementSchema.Vertical = arrangeVerticallyToTop(),
    alignment: AlignmentSchema.Horizontal = alignHorizontallyToStart(),
    isScrollable: Boolean = false,
    lazyRender: Boolean = false
) {
    addBuilder(
        ColumnSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            visibility = visibility,
            arrangement = arrangement,
            alignment = alignment,
            isScrollable = isScrollable,
            lazyRender = lazyRender
        )
    )
}
