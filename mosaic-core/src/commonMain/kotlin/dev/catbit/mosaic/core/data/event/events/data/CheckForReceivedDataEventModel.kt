package dev.catbit.mosaic.core.data.event.events.data

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDataReceivedEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnDataReceivedEventTrigger::class
    ]
)
@Serializable
@SerialName("CheckForReceivedData")
data class CheckForReceivedDataEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("dataKey") val dataKey: String
) : EventModel