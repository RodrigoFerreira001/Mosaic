package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPullEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a Material 3 pull-to-refresh container ([PullToRefreshBox]) that wraps its child
 * tiles. When the user pulls down past the threshold, a refresh indicator appears and a
 * refresh event is dispatched. The spinning indicator remains visible while [isRefreshing]
 * is `true`.
 *
 * **Updatable fields (via UpdateTiles):** `tiles: List<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `isRefreshing: Boolean`
 *
 * **Triggers dispatched:** `OnPullEventTrigger` — fired via the internal
 * `PullToRefreshTileEvents.OnRefreshStart` dispatch when the user completes the pull gesture.
 *
 * **Notes:** The server must toggle [isRefreshing] to `false` via UpdateTiles once the
 * refresh operation completes, otherwise the loading indicator will remain visible
 * indefinitely. This tile does not apply a style or visibility modifier itself; those
 * properties are inherited by the schema but not forwarded to [PullToRefreshBox] in the
 * current renderer.
 */
@Triggers(
    [
        OnPullEventTrigger::class
    ]
)
@Serializable
@SerialName("PullToRefresh")
data class PullToRefreshTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("isRefreshing") val isRefreshing: Boolean,
) : TileSchema