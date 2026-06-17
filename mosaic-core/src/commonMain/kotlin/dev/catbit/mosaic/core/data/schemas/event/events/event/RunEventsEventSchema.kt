package dev.catbit.mosaic.core.data.schemas.event.events.event

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Executes all child events in [events] unconditionally. Acts as a grouping container
 * for event chains that should always run together in response to a single trigger.
 *
 * **incomingData consumed:** Not used. Passed as-is to child events.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — after all child events have been dispatched.
 *
 * **Notes:**
 * - This event does not transform or inspect incomingData; it is a transparent dispatcher.
 * - Child events are executed via the standard `onTrigger(onSuccess())` mechanism.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("RunEvents")
data class RunEventsEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema
