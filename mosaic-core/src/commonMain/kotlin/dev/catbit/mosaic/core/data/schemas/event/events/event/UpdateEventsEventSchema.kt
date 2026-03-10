package dev.catbit.mosaic.core.data.schemas.event.events.event

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnTilesUpdatedEventTrigger::class
    ]
)
@Serializable
@SerialName("UpdateEvents")
data class UpdateEventsEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("updates") val updates: List<Update>,
) : EventSchema {

    @Serializable
    data class Update(
        @SerialName("eventId") val eventId: String,
        @SerialName("data") val data: Map<String, AnySerializable?>
    )
}