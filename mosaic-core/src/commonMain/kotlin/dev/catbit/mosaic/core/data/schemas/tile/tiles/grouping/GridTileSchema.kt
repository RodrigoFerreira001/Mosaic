package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Renders a CSS-grid-like two-dimensional layout using the experimental Compose
 * [Grid] API. Column tracks are defined by [columns] and row tracks by [rows], each
 * expressed as a [GridTrackSchema] (Fixed dp, Fraction, Flexible fr-units, Auto,
 * MaxContent, or MinContent). Items flow in the direction specified by [flow]
 * (Row-first or Column-first). Gaps between tracks are set by [columnGap] and [rowGap] in dp.
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `columns: SerializableImmutableList<GridTrackSchema>`,
 * `rows: SerializableImmutableList<GridTrackSchema>`, `columnGap: Int`, `rowGap: Int`, `flow: GridFlowSchema`
 *
 * **Triggers dispatched:** `OnDisplayEventTrigger` — fired once when the tile enters
 * composition. `OnClickEventTrigger` — fired when the grid container is tapped
 * (requires events to be wired on the schema).
 *
 * **Notes:** Uses `@OptIn(ExperimentalGridApi::class)`. The renderer exposes
 * [LocalGridScope] so that children can access grid-scoped placement modifiers such as
 * `columnSpan` and `rowSpan`. Children are rendered via `forEach` (not lazy) so all items
 * are composed eagerly.
 */
@Immutable
@Triggers([OnDisplayEventTrigger::class])
@Serializable
@SerialName("Grid")
data class GridTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
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
