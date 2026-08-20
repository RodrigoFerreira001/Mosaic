package dev.catbit.mosaic.core.data.schemas.event.events.event

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
 * Runs this event's own `events` list inside a cancellable job registered in the client's
 * `CancellableEventsHolder` under [cancellableEventId]. A later `CancelEvents` carrying the same
 * id stops the job mid-flight.
 *
 * Unlike most events, `events` here is the payload to execute: the whole list is run inline, in
 * order, receiving this event's incomingData. The job is launched in its own coroutine scope, so
 * this event returns as soon as the job is registered rather than waiting for the events to
 * finish.
 *
 * **incomingData consumed:** forwarded to the events being run.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — as soon as the job was registered, not when the events finish. No
 *   data is passed downstream.
 * - `OnFailureEventTrigger` — when `events` is `null`, so there is nothing to run.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("RunCancellableEvents")
data class RunCancellableEventsEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("cancellableEventId") val cancellableEventId: String
) : EventSchema
