package dev.catbit.mosaic.core.data.tile.tiles

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
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
data class ButtonTileModel(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("style") override val style: StyleModel
) : TileModel
