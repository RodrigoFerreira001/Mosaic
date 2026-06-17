package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a CSS flexbox-style container using the experimental Compose [FlexBox] API.
 * The layout direction is controlled by [direction] (Row, RowReverse, Column, ColumnReverse),
 * main-axis alignment by [justifyContent], cross-axis alignment of individual items by
 * [alignItems], cross-axis alignment of wrapped lines by [alignContent], and wrapping
 * behavior by [wrap]. Gaps between items are set by [columnGap] and [rowGap] in dp.
 *
 * **Updatable fields (via UpdateTiles):** `tiles: SerializableImmutableList<TileSchema>`, `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `direction: FlexDirectionSchema`,
 * `justifyContent: FlexJustifyContentSchema`, `alignItems: FlexAlignItemsSchema`,
 * `alignContent: FlexAlignContentSchema`, `wrap: FlexWrapSchema`, `columnGap: Int`,
 * `rowGap: Int`
 *
 * **Triggers dispatched:** `OnClickEventTrigger` — fired when the flex box container is
 * tapped (requires events to be wired on the schema).
 *
 * **Notes:** Uses `@OptIn(ExperimentalFlexBoxApi::class)`. The renderer exposes
 * [LocalFlexBoxScope] so that children that need [FlexBoxScope] modifiers (e.g. `flexGrow`,
 * `flexShrink`, `alignSelf`) can access it. Children are composed eagerly.
 */
@Immutable
@Serializable
@SerialName("FlexBox")
data class FlexBoxTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: SerializableImmutableList<TileSchema>,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
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
