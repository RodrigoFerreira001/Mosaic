package dev.catbit.mosaic.core.data.tile.tiles.grouping

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.GroupingTileModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.trigger.triggers.OnClickTrigger
import dev.catbit.mosaic.core.trigger.triggers.OnLongPressTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnClickTrigger::class,
        OnLongPressTrigger::class
    ]
)
@Serializable
@SerialName("Row")
data class RowTileModel(
    @SerialName("id") override val id: String,
    @SerialName("tiles") override val tiles: List<TileModel>,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("style") override val style: StyleModel,
    @SerialName("visibility") override val visibility: TileModel.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementModel.Horizontal,
    @SerialName("alignment") val alignment: AlignmentModel.Vertical
) : GroupingTileModel