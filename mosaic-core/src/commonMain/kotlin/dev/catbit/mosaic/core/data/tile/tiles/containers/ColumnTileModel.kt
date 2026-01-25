package dev.catbit.mosaic.core.data.tile.tiles.containers

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.GroupingTileModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnLongPressEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnClickEventTrigger::class,
        OnLongPressEventTrigger::class
    ]
)
@Serializable
@SerialName("Column")
data class ColumnTileModel(
    @SerialName("id") override val id: String,
    @SerialName("tiles") override val tiles: List<TileModel>,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("style") override val style: StyleModel,
    @SerialName("visibility") override val visibility: TileModel.Visibility,
    @SerialName("arrangement") val arrangement: ArrangementModel.Vertical,
    @SerialName("alignment") val alignment: AlignmentModel.Horizontal,
    @SerialName("isScrollable") val isScrollable: Boolean,
    @SerialName("lazyRender") val lazyRender: Boolean,
) : GroupingTileModel