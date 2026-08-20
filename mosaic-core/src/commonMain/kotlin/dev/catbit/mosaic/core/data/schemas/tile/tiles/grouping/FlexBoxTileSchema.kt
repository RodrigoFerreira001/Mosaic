package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Compose `FlexBox` (experimental flexbox layout) hosting [tiles] with CSS-flexbox
 * semantics. Every schema enum maps one-to-one onto the corresponding Compose flex value:
 * [direction] → main axis and its reversal, [justifyContent] → distribution along the main axis,
 * [alignItems] → alignment on the cross axis of a single line, [alignContent] → distribution of
 * the lines themselves, [wrap] → whether items wrap. [columnGap] and [rowGap] are interpreted
 * as dp.
 *
 * **Child scope:** the tile publishes its `FlexBoxScope` as a CompositionLocal, so children can
 * use flex scope modifiers (grow, shrink, basis, align-self).
 *
 * **Filtering:** when [filterChildrenByTerm] is non-null and non-empty, only children whose
 * `searchableTerms` contain that term (case-insensitive, substring match) are rendered.
 * Children without `searchableTerms` are filtered out.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnClickEventTrigger` — fired when the flex box is tapped, but **only if** an `OnClick`
 *   event is declared on this tile.
 *
 * **Notes:** [alignContent] only takes effect when [wrap] allows multiple lines. The tile is
 * never scrollable and composes every child eagerly.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnClickEventTrigger::class,
    ]
)
@Serializable
@SerialName("FlexBox")
data class FlexBoxTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("filterChildrenByTerm") val filterChildrenByTerm: String?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("direction") val direction: FlexDirectionSchema = FlexDirectionSchema.Row,
    @SerialName("justifyContent") val justifyContent: FlexJustifyContentSchema = FlexJustifyContentSchema.Start,
    @SerialName("alignItems") val alignItems: FlexAlignItemsSchema = FlexAlignItemsSchema.Start,
    @SerialName("alignContent") val alignContent: FlexAlignContentSchema = FlexAlignContentSchema.Start,
    @SerialName("wrap") val wrap: FlexWrapSchema = FlexWrapSchema.NoWrap,
    @SerialName("columnGap") val columnGap: Int = 0,
    @SerialName("rowGap") val rowGap: Int = 0
) : TileSchema {

    @Serializable
    enum class FlexDirectionSchema {
        @SerialName("row") Row,
        @SerialName("rowReverse") RowReverse,
        @SerialName("column") Column,
        @SerialName("columnReverse") ColumnReverse
    }

    @Serializable
    enum class FlexJustifyContentSchema {
        @SerialName("start") Start,
        @SerialName("center") Center,
        @SerialName("end") End,
        @SerialName("spaceBetween") SpaceBetween,
        @SerialName("spaceAround") SpaceAround,
        @SerialName("spaceEvenly") SpaceEvenly
    }

    @Serializable
    enum class FlexAlignItemsSchema {
        @SerialName("start") Start,
        @SerialName("end") End,
        @SerialName("center") Center,
        @SerialName("stretch") Stretch,
        @SerialName("baseline") Baseline
    }

    @Serializable
    enum class FlexAlignContentSchema {
        @SerialName("start") Start,
        @SerialName("end") End,
        @SerialName("center") Center,
        @SerialName("stretch") Stretch,
        @SerialName("spaceBetween") SpaceBetween,
        @SerialName("spaceAround") SpaceAround
    }

    @Serializable
    enum class FlexWrapSchema {
        @SerialName("noWrap") NoWrap,
        @SerialName("wrap") Wrap,
        @SerialName("wrapReverse") WrapReverse
    }
}
