package dev.catbit.mosaic.core.data.schemas.event.events.event

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Cancels the running cancellable context identified by [cancellableEventId], previously started
 * by a [RunCancellableEventsEventSchema]. A no-op if no context with that id is currently running.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — after the cancellation request has been dispatched.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("CancelEvents")
data class CancelEventsEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("cancellableEventId") val cancellableEventId: String
) : EventSchema
