package dev.catbit.mosaic.core.data.event.events.data

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event.data.AccessMode
import dev.catbit.mosaic.core.data.event.data.DataSource
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDataRemovedEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnDataRemovedEventTrigger::class
    ]
)
@Serializable
@SerialName("RemoveData")
data class RemoveDataEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("deletions") val deletions: List<Deletion>
) : EventModel {

    @Serializable
    data class Deletion(
        @SerialName("dataSource") val dataSource: DataSource,
        @SerialName("accessMode") val accessMode: AccessMode
    )
}