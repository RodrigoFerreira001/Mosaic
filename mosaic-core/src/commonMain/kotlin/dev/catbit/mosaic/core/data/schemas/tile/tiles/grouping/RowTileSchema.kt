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
 * Renders a Compose `Row` laying its [tiles] out horizontally, spaced by [arrangement] and
 * aligned vertically by [alignment]. When [scrollable] is `true` the row gets a
 * `horizontalScroll` modifier — every child is composed eagerly, so prefer [LazyRowTileSchema]
 * for long lists.
 *
 * **Child scope:** the row publishes its `RowScope` as a CompositionLocal (and clears the lazy
 * item and flow-row scopes), so children can use row scope modifiers such as `weight`.
 *
 * **Scroll control:** the tile listens to the screen broadcast channel and reacts to
 * scroll-to-start, scroll-to-end and scroll-to-offset commands addressed to its [id], each
 * optionally animated. The scroll-to variant takes a pixel offset.
 *
 * **Filtering:** when [filterChildrenByTerm] is non-null and non-empty, only children whose
 * `searchableTerms` contain that term (case-insensitive, substring match) are rendered.
 * Children without `searchableTerms` are filtered out.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnClickEventTrigger` — fired when the row is tapped, but **only if** an `OnClick` event is
 *   declared on this tile.
 * - `OnLongPressEventTrigger` — fired when the row is long-pressed, but **only if** an
 *   `OnLongPress` event is declared on this tile.
 * - `OnScrolledEventTrigger` — fired when the scroll direction changes, carrying
 *   `ScrollDirection.End` when scrolling forward and `ScrollDirection.Start` when scrolling
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
@SerialName("Row")
data class RowTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("filterChildrenByTerm") val filterChildrenByTerm: String?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementSchema.Horizontal,
    @SerialName("alignment") val alignment: AlignmentSchema.Vertical,
    @SerialName("scrollable") val scrollable: Boolean = false,
) : TileSchema