package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
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
 * Renders a Compose `LazyRow` over [tiles] — one lazy item per child, keyed by the child's
 * `id` — spaced by [arrangement] and aligned vertically by [alignment]. Only visible children
 * are composed, which makes this the tile to use for long or paginated horizontal lists.
 *
 * **Child scope:** each item publishes its `LazyItemScope` as a CompositionLocal (and clears the
 * row and flow-row scopes), so children can use lazy-item modifiers such as `animateItem` and
 * `fillParentMaxSize` — but **not** `RowScope.weight`.
 *
 * **Pagination:** when [scrollThreshold] is set, the tile fires its threshold trigger as soon as
 * fewer than or exactly [scrollThreshold] items remain past the last visible one. It fires at
 * most once per item count, so it will not fire again until the list actually grows —
 * [considerLoadingItemAtEndOnThresholdReached] (default `true`) additionally requires the list
 * to have grown by more than one item, which accounts for a loading placeholder appended at the
 * end while the next page is in flight.
 *
 * **Scroll control:** the tile listens to the screen broadcast channel and reacts to
 * scroll-to-start, scroll-to-end and scroll-to-item commands addressed to its [id], each
 * optionally animated. The scroll-to variant takes a child index.
 *
 * **Filtering:** when [filterChildrenByTerm] is non-null and non-empty, only children whose
 * `searchableTerms` contain that term (case-insensitive, substring match) are rendered.
 * Children without `searchableTerms` are filtered out.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnClickEventTrigger` — fired when the list itself is tapped, but **only if** an `OnClick`
 *   event is declared on this tile.
 * - `OnScrolledEventTrigger` — fired when the scroll direction changes, carrying
 *   `ScrollDirection.End` when scrolling forward and `ScrollDirection.Start` when scrolling
 *   backward.
 * - `OnScrollThresholdReachedEventTrigger` — fired as described above; never fired when
 *   [scrollThreshold] is `null`.
 *
 * **Scrollbar:** when [displayScrollbar] is `true` the tile draws a horizontal scrollbar along
 * its bottom edge. It defaults to `false` and is honoured on every platform — enable it where a
 * pointer-driven scrollbar makes sense, typically desktop and web.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnClickEventTrigger::class,
        OnScrolledEventTrigger::class,
        OnScrollThresholdReachedEventTrigger::class,
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
    @SerialName("filterChildrenByTerm") val filterChildrenByTerm: String?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementSchema.Horizontal,
    @SerialName("alignment") val alignment: AlignmentSchema.Vertical,
    @SerialName("scrollThreshold") val scrollThreshold: Int? = null,
    @SerialName("considerLoadingItemAtEndOnThresholdReached") val considerLoadingItemAtEndOnThresholdReached: Boolean = true,
    @SerialName("displayScrollbar") val displayScrollbar: Boolean = false,
) : TileSchema
