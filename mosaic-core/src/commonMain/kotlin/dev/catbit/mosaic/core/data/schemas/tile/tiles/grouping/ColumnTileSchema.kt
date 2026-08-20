package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
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
 * Renders a Compose `Column` stacking its [tiles] vertically, spaced by [arrangement] and
 * aligned horizontally by [alignment]. When [scrollable] is `true` the column gets a
 * `verticalScroll` modifier — every child is composed eagerly, so prefer [LazyColumnTileSchema]
 * for long lists.
 *
 * **Child scope:** the column publishes its `ColumnScope` as a CompositionLocal (and clears the
 * lazy-item scope), so children can use column scope modifiers such as `weight`.
 *
 * **Filtering:** when [filterChildrenByTerm] is non-null and non-empty, only children whose
 * `searchableTerms` contain that term (case-insensitive, substring match) are rendered.
 * Children without `searchableTerms` are filtered out. An empty or `null` term renders
 * everything.
 *
 * **Scroll control:** the tile listens to the screen broadcast channel and reacts to
 * scroll-to-top, scroll-to-bottom and scroll-to-offset commands addressed to its [id], each
 * optionally animated. The scroll-to variant takes a pixel offset.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnClickEventTrigger` — fired when the column is tapped, but **only if** an `OnClick` event
 *   is declared on this tile.
 * - `OnLongPressEventTrigger` — fired when the column is long-pressed, but **only if** an
 *   `OnLongPress` event is declared on this tile.
 * - `OnScrolledEventTrigger` — fired when the scroll direction changes, carrying
 *   `ScrollDirection.Bottom` when scrolling forward and `ScrollDirection.Top` when scrolling
 *   backward. Only meaningful when [scrollable] is `true`.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class,
        OnScrolledEventTrigger::class,
    ]
)
@Serializable
@SerialName("Column")
data class ColumnTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("filterChildrenByTerm") val filterChildrenByTerm: String?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementSchema.Vertical,
    @SerialName("alignment") val alignment: AlignmentSchema.Horizontal,
    @SerialName("scrollable") val scrollable: Boolean = false,
) : TileSchema