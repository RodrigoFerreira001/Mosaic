package dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FlowRow")
data class FlowRowTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("horizontalArrangement") val horizontalArrangement: ArrangementSchema.Horizontal = ArrangementSchema.Horizontal.Start,
    @SerialName("verticalArrangement") val verticalArrangement: ArrangementSchema.Vertical = ArrangementSchema.Vertical.Top,
    @SerialName("maxItemsInEachRow") val maxItemsInEachRow: Int = Int.MAX_VALUE
) : TileSchema
