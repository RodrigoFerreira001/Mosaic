package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a horizontal row that places its child tiles side by side using Compose's [Row]
 * layout. When [scrollable] is `true`, a horizontal scroll state is attached and the row can
 * be programmatically scrolled via a [RowTileBroadcastData] channel
 * (ScrollToStart, ScrollTo, ScrollToEnd).
 *
 * **Updatable fields (via UpdateTiles):** `tiles: List<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `arrangement: ArrangementSchema.Horizontal`,
 * `alignment: AlignmentSchema.Vertical`, `scrollable: Boolean`
 *
 * **Triggers dispatched:** `OnScrolledEventTrigger` — fired continuously while the user
 * scrolls, carrying `ScrollDirection.End` (forward) or `ScrollDirection.Start` (backward).
 * `OnClickEventTrigger` and `OnLongPressEventTrigger` — fired when the row itself is tapped
 * or long-pressed.
 *
 * **Notes:** The row exposes a [LocalRowScope] CompositionLocal so that direct children that
 * need a [RowScope] modifier (e.g. `weight`) can access it. Scroll broadcast commands are
 * received on the same channel type used by [LazyRowTileSchema].
 */
@Triggers(
    [
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class,
        OnScrolledEventTrigger::class
    ]
)
@Serializable
@SerialName("Row")
data class RowTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementSchema.Horizontal,
    @SerialName("alignment") val alignment: AlignmentSchema.Vertical,
    @SerialName("scrollable") val scrollable: Boolean = false,
) : TileSchema