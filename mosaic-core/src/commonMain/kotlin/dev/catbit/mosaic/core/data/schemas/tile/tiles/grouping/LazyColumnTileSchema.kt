package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

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

/**
 * Renders a vertically scrolling lazy list that only composes and lays out the items currently
 * visible on screen, using Compose's [LazyColumn]. Each child tile is keyed by its [id] for
 * stable recomposition. The list state can be programmatically controlled via
 * [ColumnTileBroadcastData] (ScrollToTop, ScrollTo, ScrollToBottom).
 *
 * **Updatable fields (via UpdateTiles):** `tiles: List<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `arrangement: ArrangementSchema.Vertical`,
 * `alignment: AlignmentSchema.Horizontal`, `scrollThreshold: Int?`,
 * `considerLoadingItemAtEndOnThresholdReached: Boolean`
 *
 * **Triggers dispatched:**
 * - `OnScrolledEventTrigger` — fired while scrolling, with `ScrollDirection.Bottom` (forward)
 *   or `ScrollDirection.Top` (backward).
 * - `OnScrollThresholdReachedEventTrigger` — fired when the user scrolls within
 *   [scrollThreshold] items of the end of the list. Only active when [scrollThreshold] is
 *   non-null.
 * - `OnClickEventTrigger` and `OnLongPressEventTrigger` — fired when the list container is
 *   tapped or long-pressed.
 *
 * **Notes:** Uses the same [ColumnTileBroadcastData] broadcast channel as [ColumnTileSchema],
 * so both tile types respond to the same scroll commands. When
 * [considerLoadingItemAtEndOnThresholdReached] is `true`, the threshold calculation accounts
 * for a loading placeholder item appended at the end of the list. The renderer sets
 * [LocalLazyColumnRenderingScope] so that children that need lazy list item modifiers can
 * detect they are inside a lazy context.
 */
@Triggers(
    [
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class,
        OnScrolledEventTrigger::class,
        OnScrollThresholdReachedEventTrigger::class
    ]
)
@Serializable
@SerialName("LazyColumn")
data class LazyColumnTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementSchema.Vertical,
    @SerialName("alignment") val alignment: AlignmentSchema.Horizontal,
    @SerialName("scrollThreshold") val scrollThreshold: Int? = null,
    @SerialName("considerLoadingItemAtEndOnThresholdReached") val considerLoadingItemAtEndOnThresholdReached: Boolean = true,
) : TileSchema
