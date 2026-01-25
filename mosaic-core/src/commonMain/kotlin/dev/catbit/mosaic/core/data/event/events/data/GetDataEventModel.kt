package dev.catbit.mosaic.core.data.event.events.data

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event.data.AccessMode
import dev.catbit.mosaic.core.data.event.data.DataSource
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnStartEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("GetData")
data class GetDataEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("readings") val readings: List<Reading>
) : EventModel {

    @Serializable
    data class Reading(
        @SerialName("dataSource") val dataSource: DataSource,
        @SerialName("accessMode") val accessMode: AccessMode
    )
}