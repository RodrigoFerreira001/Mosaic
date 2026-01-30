package dev.catbit.mosaic.core.data.schemas.tile.tiles.containers

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.GroupingTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.AlignmentSchema
import dev.catbit.mosaic.core.data.schemas.tile.placement.ArrangementSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger::class,
        dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger::class
    ]
)
@Serializable
@SerialName("Column")
data class ColumnTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") override val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementSchema.Vertical,
    @SerialName("alignment") val alignment: AlignmentSchema.Horizontal,
    @SerialName("isScrollable") val isScrollable: Boolean,
    @SerialName("lazyRender") val lazyRender: Boolean,
) : GroupingTileSchema