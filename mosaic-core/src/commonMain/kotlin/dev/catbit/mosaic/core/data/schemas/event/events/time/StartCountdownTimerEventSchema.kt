package dev.catbit.mosaic.core.data.schemas.event.events.time

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerTickEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnCountdownTimerFinishEventTrigger::class,
        OnCountdownTimerTickEventTrigger::class,
    ]
)
@Serializable
@SerialName("StartCountdownTimer")
data class StartCountdownTimerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("setupTimeInSeconds") val setupTimeInSeconds: Long
) : EventSchema
