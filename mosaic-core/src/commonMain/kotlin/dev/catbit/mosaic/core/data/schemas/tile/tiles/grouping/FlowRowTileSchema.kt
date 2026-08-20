package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Compose `FlowRow` laying its [tiles] out horizontally and wrapping onto new lines
 * when they no longer fit. [horizontalArrangement] spaces items within a line,
 * [verticalArrangement] spaces the lines themselves, and [maxItemsInEachRow] caps how many
 * children a single line may hold (defaults to unlimited).
 *
 * **Child scope:** the tile publishes its `FlowRowScope` as a CompositionLocal (and clears the
 * row and lazy-item scopes), so children can use flow-row scope modifiers such as `weight` and
 * `fillMaxRowHeight`.
 *
 * **Filtering:** when [filterChildrenByTerm] is non-null and non-empty, only children whose
 * `searchableTerms` contain that term (case-insensitive, substring match) are rendered.
 * Children without `searchableTerms` are filtered out.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnClickEventTrigger` — fired when the flow row is tapped, but **only if** an `OnClick`
 *   event is declared on this tile.
 *
 * **Notes:** the tile is never scrollable and composes every child eagerly.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnClickEventTrigger::class,
    ]
)
@Serializable
@SerialName("FlowRow")
data class FlowRowTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("filterChildrenByTerm") val filterChildrenByTerm: String?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("horizontalArrangement") val horizontalArrangement: ArrangementSchema.Horizontal = ArrangementSchema.Horizontal.Start,
    @SerialName("verticalArrangement") val verticalArrangement: ArrangementSchema.Vertical = ArrangementSchema.Vertical.Top,
    @SerialName("maxItemsInEachRow") val maxItemsInEachRow: Int = Int.MAX_VALUE
) : TileSchema
