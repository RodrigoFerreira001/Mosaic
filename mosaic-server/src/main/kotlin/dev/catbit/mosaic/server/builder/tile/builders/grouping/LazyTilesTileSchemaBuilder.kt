package dev.catbit.mosaic.server.builder.tile.builders.grouping

import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyTilesTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class LazyTilesTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
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
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        failureTiles = TileSchemaBuilderScope().apply(failureTiles).build(),
        placeholderTiles = TileSchemaBuilderScope().apply(placeholderTiles).build(),
        url = url,
        method = method,
        body = body,
        headers = headers
    )
}

/**
 * Renders a plain `Column` whose content is fetched from the network at display time: the tile
 * calls [url] with [method], [body] and [headers], expects a JSON array of tile schemas back,
 * and renders them in place of [placeholderTiles]. [placeholderTiles] are shown while the
 * request is in flight (fired once, on the IO dispatcher), and [failureTiles] replace them if
 * the request or the response decoding fails. A local reload event clears the loaded state and
 * retries. The request is issued directly by the renderer, not through the event pipeline, so it
 * is not affected by event chaining. Children are laid out with no scope CompositionLocal and no
 * scrolling.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (`onDisplay`, load-start, load-success, load-failure).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param url URL requested to fetch the JSON array of tile schemas to render.
 * @param method HTTP method used for the request. Defaults to GET.
 * @param body Request body sent with the request. Defaults to none.
 * @param headers Request headers sent with the request. Defaults to none.
 * @param failureTiles Tiles rendered when the request or response decoding fails. Defaults to none.
 * @param placeholderTiles Tiles rendered while the request is in flight. Defaults to none.
 */
fun TileSchemaBuilderScope.LazyTiles(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
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
            searchableTerms = searchableTerms,
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
