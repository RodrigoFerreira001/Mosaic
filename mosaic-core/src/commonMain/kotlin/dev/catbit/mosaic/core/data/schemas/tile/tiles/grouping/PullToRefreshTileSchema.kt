package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPullEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Material 3 `PullToRefreshBox` wrapping [tiles], showing the refresh indicator while
 * [isRefreshing] is `true`.
 *
 * **Refresh lifecycle:** pulling dispatches a local `PullToRefreshTileEvents.OnRefreshStart`,
 * and the holder immediately sets [isRefreshing] to `true` so the spinner appears without a
 * server round trip. Stopping it is the server's job: chain a `StopRefreshingEventSchema`
 * pointing at this tile's [id] onto the end of the refresh flow — on both the success and the
 * failure branch — otherwise the indicator keeps spinning.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnPullEventTrigger` — fired when the user completes a pull gesture; hook the refresh work
 *   to this trigger.
 *
 * **Notes:** children are laid out with `Box` semantics and no scope CompositionLocal; put a
 * scrollable tile inside for the gesture to feel natural.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnPullEventTrigger::class,
    ]
)
@Serializable
@SerialName("PullToRefresh")
data class PullToRefreshTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("isRefreshing") val isRefreshing: Boolean = false,
) : TileSchema