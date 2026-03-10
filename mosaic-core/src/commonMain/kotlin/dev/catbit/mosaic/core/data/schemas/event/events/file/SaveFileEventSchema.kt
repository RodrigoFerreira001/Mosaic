package dev.catbit.mosaic.core.data.schemas.event.events.file

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("SaveFile")
data class SaveFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("fileName") val fileName: String,
    @SerialName("overrideIfExists") val overrideIfExists: Boolean
) : EventSchema
