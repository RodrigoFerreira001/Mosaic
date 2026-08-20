package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a `Column` whose content is fetched from the network at display time: the tile calls
 * [url] with [method], [headers] and [body], expects a JSON array of tile schemas back, and
 * renders them in place of its placeholder.
 *
 * **States:**
 * - [tiles] non-null → the loaded tiles are rendered and no request is made.
 * - [tiles] `null` and [isFailureState] `false` → [placeholderTiles] are rendered while the
 *   request runs (fired once via a single-shot effect, on the IO dispatcher).
 * - [tiles] `null` and [isFailureState] `true` → [failureTiles] are rendered and no request is
 *   made.
 *
 * On success the renderer dispatches a local `LazyTilesTileEvents.OnTilesLoadedSuccessfully`
 * and the holder builds real tile holders from the decoded schemas; on a transport error **or**
 * a decoding error it dispatches `OnTilesLoadFailure` and the holder flips [isFailureState].
 * A local `OnReloadTiles` event clears the loaded tiles and the failure flag, putting the tile
 * back into its loading state.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnLoadTilesStartEventTrigger` — right before the request is sent.
 * - `OnLoadTilesSuccessEventTrigger` — after the response has been received **and** decoded, so
 *   exactly one of this and the failure trigger fires per attempt.
 * - `OnLoadTilesFailureEventTrigger` — on a request or decoding failure; the `Throwable` is
 *   passed as the event's incoming data.
 *
 * **Notes:** the request is issued directly by the renderer, not through the event pipeline, so
 * it is not affected by event chaining. Children are laid out in a plain `Column` with no scope
 * CompositionLocal and no scrolling.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnLoadTilesStartEventTrigger::class,
        OnLoadTilesSuccessEventTrigger::class,
        OnLoadTilesFailureEventTrigger::class,
    ]
)
@SerialName("LazyTiles")
@Serializable
data class LazyTilesTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>? = null,
    @SerialName("placeholderTiles") val placeholderTiles: SerializableImmutableList<TileSchema>,
    @SerialName("failureTiles") val failureTiles: SerializableImmutableList<TileSchema>,
    @SerialName("isFailureState") val isFailureState: Boolean = false,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?
) : TileSchema
