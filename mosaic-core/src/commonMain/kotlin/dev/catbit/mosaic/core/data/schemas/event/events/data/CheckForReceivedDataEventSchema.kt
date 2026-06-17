package dev.catbit.mosaic.core.data.schemas.event.events.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataReceivedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Checks the [DataMailer] in-process message bus for a value stored under [dataKey] and, if
 * one exists, forwards it to downstream events. This is the "receive" half of the
 * [SendDataEventSchema] / [CheckForReceivedDataEventSchema] data-passing pair.
 *
 * **incomingData consumed:** Not used. This event reads from [DataMailer], not from the
 * incoming data chain.
 *
 * **Triggers fired:**
 * - [OnDataReceivedEventTrigger] — only when [DataMailer.getData] returns a non-null value for
 *   [dataKey]. The retrieved value becomes the new `incomingData` for downstream events.
 * - [OnSuccessEventTrigger] — fired after [OnDataReceivedEventTrigger] when data is found.
 * - [OnFailureEventTrigger] — fired when no value is found for [dataKey] in [DataMailer].
 *
 * **Failure scenarios:**
 * - If [dataKey] has no value in [DataMailer] (either never sent or already consumed),
 *   [OnFailureEventTrigger] fires with null incomingData.
 *
 * **Notes:**
 * - [DataMailer] is an in-process, non-persistent bus. Values survive only for the current
 *   app session. Use [SendDataEventSchema] to populate it.
 * - Whether [DataMailer.getData] is a destructive read (consume-once) or a peek depends on
 *   the [DataMailer] implementation — this schema does not specify retention semantics.
 * - This event is synchronous: it does not suspend or dispatch to a background dispatcher.
 */
@Immutable
@Triggers(
    [
        OnDataReceivedEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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