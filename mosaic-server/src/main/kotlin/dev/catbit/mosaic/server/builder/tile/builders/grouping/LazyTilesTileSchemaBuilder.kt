package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyTilesTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class LazyTilesTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val failureTiles: TileSchemaBuilderScope.() -> Unit,
    private val placeholderTiles: TileSchemaBuilderScope.() -> Unit,
    private val url: String,
    private val method: HttpMethod,
    private val body: AnySerializable?,
    private val headers: Map<String, String>?
) : TileSchemaBuilder<LazyTilesTileSchema>() {

    override fun build() = LazyTilesTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        failureTiles = TileSchemaBuilderScope().apply(failureTiles).build(),
        placeholderTiles = TileSchemaBuilderScope().apply(placeholderTiles).build(),
        url = url,
        method = method,
        body = body,
        headers = headers
    )
}

fun TileSchemaBuilderScope.LazyTiles(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    url: String,
    method: HttpMethod = HttpMethod.GET,
    body: AnySerializable? = null,
    headers: Map<String, String>? = null,
    failureTiles: TileSchemaBuilderScope.() -> Unit = {},
    placeholderTiles: TileSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        LazyTilesTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            failureTiles = failureTiles,
            placeholderTiles = placeholderTiles,
            url = url,
            method = method,
            body = body,
            headers = headers
        )
    )
}
