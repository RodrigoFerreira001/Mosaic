package dev.catbit.mosaic.server.builder.tile.builders.containers

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.BoxTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class BoxSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    private val visibility: TileSchema.Visibility,
    private val alignment: AlignmentSchema.TwoDimensional
) : TileSchemaBuilder<BoxTileSchema> {

    override fun build() = BoxTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        alignment = alignment
    )
}

fun TileSchemaBuilderScope.Box(
    id: String = randomUuid(),
    tiles: TileSchemaBuilderScope.() -> Unit,
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    alignment: AlignmentSchema.TwoDimensional = alignToTopStart()
) {
    addBuilder(
        BoxSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            visibility = visibility,
            alignment = alignment
        )
    )
}
