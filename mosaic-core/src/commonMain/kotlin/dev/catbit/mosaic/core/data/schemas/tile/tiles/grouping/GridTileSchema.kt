package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Renders a Compose `Grid` (experimental CSS-grid-like layout) hosting [tiles]. [columns] and
 * [rows] declare the track template, each track being one of:
 * [GridTrackSchema.Fixed] (dp), [GridTrackSchema.Fraction] (fraction of the available space),
 * [GridTrackSchema.Flexible] (`fr` unit), [GridTrackSchema.Auto], [GridTrackSchema.MaxContent]
 * or [GridTrackSchema.MinContent]. [flow] decides whether children are placed row-first or
 * column-first, and [columnGap] / [rowGap] are interpreted as dp.
 *
 * **Child scope:** the tile publishes its `GridScope` as a CompositionLocal, so children can use
 * grid scope modifiers (row/column span and placement).
 *
 * **Filtering:** when [filterChildrenByTerm] is non-null and non-empty, only children whose
 * `searchableTerms` contain that term (case-insensitive, substring match) are rendered.
 * Children without `searchableTerms` are filtered out.
 *
 * **Triggers dispatched:**
 * - `OnDisplayEventTrigger` — fired once when the tile enters composition (keyed by tile id).
 * - `OnClickEventTrigger` — fired when the grid is tapped, but **only if** an `OnClick` event is
 *   declared on this tile.
 * - `OnLongPressEventTrigger` — fired when the grid is long-pressed, but **only if** an
 *   `OnLongPress` event is declared on this tile.
 *
 * **Notes:** [rows] defaults to empty, in which case row tracks are derived implicitly. The tile
 * is never scrollable and composes every child eagerly.
 */
@Immutable
@Triggers(
    [
        OnDisplayEventTrigger::class,
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class,
    ]
)
@Serializable
@SerialName("Grid")
data class GridTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("filterChildrenByTerm") val filterChildrenByTerm: String?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("columns") val columns: SerializableImmutableList<GridTrackSchema>,
    @SerialName("rows") val rows: SerializableImmutableList<GridTrackSchema> = persistentListOf(),
    @SerialName("columnGap") val columnGap: Int = 0,
    @SerialName("rowGap") val rowGap: Int = 0,
    @SerialName("flow") val flow: GridFlowSchema = GridFlowSchema.Row
) : TileSchema {

    @Serializable
    sealed interface GridTrackSchema {

        @Serializable
        @SerialName("fixed")
        data class Fixed(
            @SerialName("value") val value: Int
        ) : GridTrackSchema

        @Serializable
        @SerialName("fraction")
        data class Fraction(
            @SerialName("value") val value: Float
        ) : GridTrackSchema

        @Serializable
        @SerialName("flexible")
        data class Flexible(
            @SerialName("value") val value: Float
        ) : GridTrackSchema

        @Serializable
        @SerialName("auto")
        data object Auto : GridTrackSchema

        @Serializable
        @SerialName("maxContent")
        data object MaxContent : GridTrackSchema

        @Serializable
        @SerialName("minContent")
        data object MinContent : GridTrackSchema
    }

    @Serializable
    enum class GridFlowSchema {
        Row, Column
    }
}
