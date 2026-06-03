package dev.catbit.mosaic.core.data.schemas.event.events.data

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Delegates `incomingData` to a client-registered [DataProcessor] identified by [processWith],
 * allowing arbitrary platform-side logic to be applied to the data. Processors are registered
 * in the Koin graph; the first one whose `id` matches [processWith] is used.
 *
 * **incomingData consumed:** Passed directly to [DataProcessor.process]. The processor is
 * responsible for calling either its `onSuccess` or `onFailure` callback with any result value.
 *
 * **Triggers fired:**
 * - [onSuccess()] – Called by the matched [DataProcessor] via its `onSuccess` callback. Note
 *   that the runner passes no data to [onSuccess] — it is up to the processor implementation
 *   whether to propagate data through a side channel.
 * - [onFailure(data)] – Called by the matched [DataProcessor] via its `onFailure` callback,
 *   where `data` is whatever failure value the processor provides.
 *
 * **Failure scenarios:**
 * - If `incomingData` is null, the event is a complete no-op: no processor is invoked, no
 *   trigger fires, no error is reported.
 * - If no registered [DataProcessor] has an `id` matching [processWith], the event is also a
 *   no-op: no trigger fires, no error is reported.
 *
 * **Notes:**
 * - [DataProcessor] implementations are registered via Koin and looked up with `getAll<DataProcessor>()`.
 *   Only the **first** matching processor in the list is executed; subsequent ones are ignored.
 * - The processor's `onSuccess` callback fires [EventTriggers.onSuccess()] with no data argument,
 *   meaning downstream events receive null as `incomingData` unless the processor produces output
 *   through a different mechanism (e.g. [UpdateDataEventSchema] or [SendDataEventSchema]).
 * - This event is synchronous at the runner level; the processor itself may be asynchronous
 *   depending on its implementation contract.
 */
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("ProcessData")
data class ProcessDataEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("processWith") val processWith: String,
) : EventSchema