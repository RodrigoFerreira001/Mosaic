package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema.CarouselTypeSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class CarouselTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val type: CarouselTypeSchema,
    private val itemSpacing: Int,
    private val contentPadding: Int,
    private val userScrollEnabled: Boolean
) : TileSchemaBuilder<CarouselTileSchema>() {

    override fun build() = CarouselTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        type = type,
        itemSpacing = itemSpacing,
        contentPadding = contentPadding,
        userScrollEnabled = userScrollEnabled
    )
}

fun TileSchemaBuilderScope.Carousel(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    type: CarouselTypeSchema,
    itemSpacing: Int = 0,
    contentPadding: Int = 0,
    userScrollEnabled: Boolean = true,
    tiles: TileSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        CarouselTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            visibility = visibility,
            type = type,
            itemSpacing = itemSpacing,
            contentPadding = contentPadding,
            userScrollEnabled = userScrollEnabled
        )
    )
}

fun multiBrowse(
    preferredItemWidth: Int,
    minSmallItemWidth: Int? = null,
    maxSmallItemWidth: Int? = null
): CarouselTypeSchema.MultiBrowse = CarouselTypeSchema.MultiBrowse(
    preferredItemWidth = preferredItemWidth,
    minSmallItemWidth = minSmallItemWidth,
    maxSmallItemWidth = maxSmallItemWidth
)

fun uncontained(
    itemWidth: Int
): CarouselTypeSchema.Uncontained = CarouselTypeSchema.Uncontained(
    itemWidth = itemWidth
)
