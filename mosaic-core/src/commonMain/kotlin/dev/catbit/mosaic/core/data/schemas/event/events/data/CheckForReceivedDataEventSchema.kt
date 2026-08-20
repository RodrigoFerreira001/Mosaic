package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataReceivedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Reads a one-shot value from the client's `DataMailer` under [dataKey] and branches on whether
 * it was there.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnDataReceivedEventTrigger` — when a value exists for [dataKey]; the value is passed as
 *   incomingData.
 * - `OnSuccessEventTrigger` — right after the trigger above, with the same value as incomingData.
 * - `OnFailureEventTrigger` — when no value exists for [dataKey]; no incomingData is passed.
 */
@Immutable
@Triggers(
    [
        OnDataReceivedEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("CheckForReceivedData")
data class CheckForReceivedDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("dataKey") val dataKey: String
) : EventSchema