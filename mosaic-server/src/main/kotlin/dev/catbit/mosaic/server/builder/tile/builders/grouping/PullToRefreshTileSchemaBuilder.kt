package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PullToRefreshTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class PullToRefreshTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val isRefreshing: Boolean
) : TileSchemaBuilder<PullToRefreshTileSchema>() {

    override fun build() = PullToRefreshTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        isRefreshing = isRefreshing
    )
}

fun TileSchemaBuilderScope.PullToRefresh(
    id: String = randomId(),
    tiles: TileSchemaBuilderScope.() -> Unit,
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    searchableTerms: List<String>? = null,
    isRefreshing: Boolean
) {
    addBuilder(
        PullToRefreshTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            isRefreshing = isRefreshing
        )
    )
}
