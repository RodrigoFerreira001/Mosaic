package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Hands the event's incomingData to the `DataProcessor` registered by the client under the id
 * [processWith]. Processors are supplied by the host application, so what the processing does is
 * opaque to the framework.
 *
 * **incomingData consumed:** required — it is the value passed to the processor.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the processor returns successfully. No data is passed
 *   downstream.
 * - `OnFailureEventTrigger` — when the processor returns a failure (the `Throwable` is passed as
 *   incomingData), when no processor is registered under [processWith], or when incomingData is
 *   `null` (no data passed in those two cases).
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("ProcessData")
data class ProcessDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("processWith") val processWith: String,
) : EventSchema