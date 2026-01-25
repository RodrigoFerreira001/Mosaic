package dev.catbit.mosaic.core.data.event.events.data

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event.data.AccessMode
import dev.catbit.mosaic.core.data.event.data.DataSource
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnDataUpdatedEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnDataUpdatedEventTrigger::class
    ]
)
@Serializable
@SerialName("UpdateData")
data class UpdateDataEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("updates") val updates: List<Update>
) : EventModel {

    @Serializable
    data class Update(
        @SerialName("dataSource") val dataSource: DataSource,
        @SerialName("accessMode") val accessMode: AccessMode
    )
}