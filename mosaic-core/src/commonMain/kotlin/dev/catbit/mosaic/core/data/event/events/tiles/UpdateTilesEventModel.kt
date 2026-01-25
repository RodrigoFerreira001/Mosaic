package dev.catbit.mosaic.core.data.event.events.tiles

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTilesUpdatedEventTrigger::class
    ]
)
@Serializable
@SerialName("UpdateTiles")
data class UpdateTilesEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("updates") val updates: List<Update>,
) : EventModel {

    @Serializable
    data class Update(
        @SerialName("tileId") val tileId: String,
        @SerialName("data") val data: Map<String, AnySerializable>
    )
}