package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a self-loading tile container that fetches its child tiles from a remote endpoint
 * on first composition. While loading, [placeholderTiles] are rendered. On success the
 * fetched tiles replace the placeholder. On failure, if [isFailureState] is `true`, the
 * [failureTiles] are rendered instead.
 *
 * **Updatable fields (via UpdateTiles):** `tiles: List<TileSchema>?`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `isFailureState: Boolean`, `placeholderTiles`,
 * `failureTiles`, `url: String`, `method: HttpMethod`, `body: AnySerializable?`,
 * `headers: Map<String, String>?`
 *
 * **Triggers dispatched:**
 * - `OnLoadTilesStartEventTrigger` — fired immediately before the network request is sent.
 * - `OnLoadTilesSuccessEventTrigger` — fired after the response is successfully decoded into
 *   a list of [TileSchema].
 * - `OnLoadTilesFailureEventTrigger` — fired if the network request fails or if JSON
 *   deserialization throws. The throwable is passed as trigger data.
 *
 * **Notes:** The network call is executed exactly once per composition lifecycle via
 * `SingleEffect` (a one-shot side-effect launcher). If [tiles] is already non-null in the
 * schema (e.g. populated by UpdateTiles), the fetch is skipped and the existing tiles are
 * rendered directly. The response body is decoded as `List<TileSchema>` using the polymorphic
 * [MosaicSerializer]. The outer layout is always a [Column] with the given [style].
 */
@SerialName("LazyTiles")
@Serializable
data class LazyTilesTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("tiles") val tiles: List<TileSchema>? = null,
    @SerialName("placeholderTiles") val placeholderTiles: List<TileSchema>,
    @SerialName("failureTiles") val failureTiles: List<TileSchema>,
    @SerialName("isFailureState") val isFailureState: Boolean = false,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?
) : TileSchema
