package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.RowTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignVerticallyToTop
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallyToStart
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class RowTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val arrangement: ArrangementSchema.Horizontal,
    private val alignment: AlignmentSchema.Vertical,
    private val isScrollable: Boolean,
    private val lazyRender: Boolean
) : TileSchemaBuilder<RowTileSchema>() {

    override fun build() = RowTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        arrangement = arrangement,
        alignment = alignment,
        isScrollable = isScrollable,
        lazyRender = lazyRender
    )
}

fun TileSchemaBuilderScope.Row(
    id: String = randomUuid(),
    tiles: TileSchemaBuilderScope.() -> Unit,
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    arrangement: ArrangementSchema.Horizontal = arrangeHorizontallyToStart(),
    alignment: AlignmentSchema.Vertical = alignVerticallyToTop(),
    isScrollable: Boolean = false,
    lazyRender: Boolean = false
) {
    addBuilder(
        RowTileSchemaBuilder(
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
