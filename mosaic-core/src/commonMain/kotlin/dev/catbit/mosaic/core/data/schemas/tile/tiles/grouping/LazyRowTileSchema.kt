package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrollThresholdReachedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a horizontally scrolling lazy list that only composes and lays out the items
 * currently visible on screen, using Compose's [LazyRow]. Each child tile is keyed by its
 * [id] for stable recomposition. The list state can be programmatically controlled via
 * [RowTileBroadcastData] (ScrollToStart, ScrollTo, ScrollToEnd).
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `arrangement: ArrangementSchema.Horizontal`,
 * `alignment: AlignmentSchema.Vertical`, `scrollThreshold: Int?`,
 * `considerLoadingItemAtEndOnThresholdReached: Boolean`
 *
 * **Triggers dispatched:**
 * - `OnScrolledEventTrigger` — fired while scrolling, with `ScrollDirection.End` (forward) or
 *   `ScrollDirection.Start` (backward).
 * - `OnScrollThresholdReachedEventTrigger` — fired when the user scrolls within
 *   [scrollThreshold] items of the end of the list. Only active when [scrollThreshold] is
 *   non-null.
 * - `OnClickEventTrigger` and `OnLongPressEventTrigger` — fired when the list container is
 *   tapped or long-pressed.
 *
 * **Notes:** Uses the same [RowTileBroadcastData] broadcast channel as [RowTileSchema], so
 * both tile types respond to the same scroll commands. When
 * [considerLoadingItemAtEndOnThresholdReached] is `true`, the threshold calculation accounts
 * for a loading placeholder item appended at the trailing end of the list. The renderer sets
 * [LocalLazyRowRenderingScope] so that children can detect they are inside a lazy context.
 */
@Immutable
@Triggers(
    [
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class,
        OnScrolledEventTrigger::class,
        OnScrollThresholdReachedEventTrigger::class
    ]
)
@Serializable
@SerialName("LazyRow")
data class LazyRowTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementSchema.Horizontal,
    @SerialName("alignment") val alignment: AlignmentSchema.Vertical,
    @SerialName("scrollThreshold") val scrollThreshold: Int? = null,
    @SerialName("considerLoadingItemAtEndOnThresholdReached") val considerLoadingItemAtEndOnThresholdReached: Boolean = true,
) : TileSchema
