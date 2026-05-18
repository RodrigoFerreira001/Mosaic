package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Grid")
data class GridTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("columns") val columns: List<GridTrackSchema>,
    @SerialName("rows") val rows: List<GridTrackSchema> = emptyList(),
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
