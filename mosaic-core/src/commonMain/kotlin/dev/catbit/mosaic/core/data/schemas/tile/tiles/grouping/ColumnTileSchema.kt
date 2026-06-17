package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
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
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a vertical column that stacks its child tiles from top to bottom using Compose's
 * [Column] layout. When [scrollable] is `true`, a vertical scroll state is attached and the
 * column can be programmatically scrolled via a [ColumnTileBroadcastData] channel
 * (ScrollToTop, ScrollTo, ScrollToBottom).
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `arrangement: ArrangementSchema.Vertical`,
 * `alignment: AlignmentSchema.Horizontal`, `scrollable: Boolean`
 *
 * **Triggers dispatched:** `OnScrolledEventTrigger` — fired continuously while the user
 * scrolls, carrying `ScrollDirection.Bottom` (forward) or `ScrollDirection.Top` (backward).
 * `OnClickEventTrigger` and `OnLongPressEventTrigger` — fired when the column itself is tapped
 * or long-pressed (requires events to be wired on the schema).
 *
 * **Notes:** The column exposes a [LocalColumnScope] CompositionLocal so that direct children
 * that need a [ColumnScope] modifier (e.g. `weight`) can access it. Scroll broadcast commands
 * are received on the same channel type used by [LazyColumnTileSchema].
 */
@Immutable
@Triggers(
    [
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class,
        OnScrolledEventTrigger::class
    ]
)
@Serializable
@SerialName("Column")
data class ColumnTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementSchema.Vertical,
    @SerialName("alignment") val alignment: AlignmentSchema.Horizontal,
    @SerialName("scrollable") val scrollable: Boolean = false,
) : TileSchema