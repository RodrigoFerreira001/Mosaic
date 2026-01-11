package dev.catbit.mosaic.core.data.tile.tiles.buttons

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
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
@SerialName("Button")
data class ButtonTileModel(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("style") override val style: StyleModel,
    @SerialName("visibility") override val visibility: TileModel.Visibility,
    @SerialName("text") val text: String,
    @SerialName("loading") val loading: Boolean
) : TileModel