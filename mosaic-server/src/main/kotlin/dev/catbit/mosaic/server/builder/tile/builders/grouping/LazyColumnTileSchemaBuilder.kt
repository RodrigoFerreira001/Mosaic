package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyColumnTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignHorizontallyToStart
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallyToTop
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class LazyColumnTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val arrangement: ArrangementSchema.Vertical,
    private val alignment: AlignmentSchema.Horizontal,
    private val scrollThreshold: Int?,
    private val considerLoadingItemAtEndOnThresholdReached: Boolean = true,
) : TileSchemaBuilder<LazyColumnTileSchema>() {

    override fun build() = LazyColumnTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        arrangement = arrangement,
        alignment = alignment,
        scrollThreshold = scrollThreshold,
        considerLoadingItemAtEndOnThresholdReached = considerLoadingItemAtEndOnThresholdReached
    )
}

fun TileSchemaBuilderScope.LazyColumn(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    arrangement: ArrangementSchema.Vertical = arrangeVerticallyToTop(),
    alignment: AlignmentSchema.Horizontal = alignHorizontallyToStart(),
    scrollThreshold: Int? = null,
    considerLoadingItemAtEndOnThresholdReached: Boolean = true,
    tiles: TileSchemaBuilderScope.() -> Unit,
) {
    addBuilder(
        LazyColumnTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            visibility = visibility,
            arrangement = arrangement,
            alignment = alignment,
            scrollThreshold = scrollThreshold,
            considerLoadingItemAtEndOnThresholdReached = considerLoadingItemAtEndOnThresholdReached
        )
    )
}
