package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Posts a value into the client's `DataMailer` under [dataKey], where a later
 * `CheckForReceivedData` can pick it up. The mailer is a one-shot channel, so this is the way to
 * hand a value to another screen.
 *
 * The value sent is [data] when it is non-null, otherwise the event's incomingData.
 *
 * **incomingData consumed:** used as the value to send whenever [data] is `null`.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — after the value was posted. No data is passed downstream.
 * - `OnFailureEventTrigger` — when both [data] and incomingData are `null`, so there is nothing to
 *   send; the error is logged and no data is passed.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("SendData")
data class SendDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("dataKey") val dataKey: String,
    @SerialName("data") val data: AnySerializable?
) : EventSchema