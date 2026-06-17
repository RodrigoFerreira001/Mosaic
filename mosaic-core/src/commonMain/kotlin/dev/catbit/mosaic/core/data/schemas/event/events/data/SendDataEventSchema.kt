package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Posts a value into the [DataMailer] in-process message bus under a named [dataKey], making it
 * available for retrieval by [CheckForReceivedDataEventSchema] elsewhere in the event graph.
 * This is the "send" half of a lightweight inter-screen data-passing mechanism.
 *
 * **incomingData consumed:** Used as the payload when [data] is `null`. The runner applies the
 * rule `(data ?: incomingData)` — the inline [data] field takes precedence over `incomingData`.
 * If both are null, nothing is sent and the event fires [OnFailureEventTrigger].
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when data is posted successfully to [DataMailer].
 * - [OnFailureEventTrigger] — when both [data] and `incomingData` are null; nothing is posted.
 *
 * **Failure scenarios:**
 * - If both [data] and `incomingData` are null, [OnFailureEventTrigger] fires and nothing is
 *   stored in [DataMailer].
 *
 * **Notes:**
 * - [DataMailer] operates in-process; it is not a network call or persistent store.
 * - The payload is keyed by [dataKey] and overwrites any previously sent value under the same key.
 * - Pair this event with [CheckForReceivedDataEventSchema] (using the same [dataKey]) on the
 *   receiving side to retrieve the value.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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