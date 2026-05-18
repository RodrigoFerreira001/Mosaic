package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FlexBox")
data class FlexBoxTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
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
