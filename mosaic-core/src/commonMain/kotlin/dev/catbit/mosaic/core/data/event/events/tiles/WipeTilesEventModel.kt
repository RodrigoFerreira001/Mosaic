package dev.catbit.mosaic.core.data.event.events.tiles

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTilesWipedEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTilesWipedEventTrigger::class
    ]
)
@Serializable
@SerialName("WipeTiles")
data class WipeTilesEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    val groupingTileId: String
) : EventModel
