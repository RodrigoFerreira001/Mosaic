package dev.catbit.mosaic.core.data.schemas.event.events.event

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Looks up another registered event by its [eventId] and runs it inline within the current
 * execution context. This allows a server-defined event to act as a reusable subroutine that
 * can be invoked from multiple call sites without duplicating event definitions.
 *
 * **incomingData consumed:** Not used by this event itself. However, the resolved target event
 * may read incomingData from its own holder, not from the caller's context.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — when the resolved event is found and executed without throwing.
 * - [OnFailureEventTrigger] — if [eventId] does not match any registered event, or if the
 *   resolved event throws an exception; incomingData is null or the exception.
 *
 * **Failure scenarios:**
 * - If [eventId] does not match any event currently registered in the [tilesEventDispatcher],
 *   [OnFailureEventTrigger] fires.
 * - Errors thrown inside the resolved event fire [OnFailureEventTrigger] with the exception.
 *
 * **Notes:** The target event is executed synchronously within the same coroutine scope as the
 * caller (`runEventInline`). This means trigger chains from the resolved event complete before
 * the caller's downstream events continue. Circular references between events would cause a
 * stack overflow and should be avoided.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("TriggerEvent")
data class TriggerEventEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("eventId") val eventId: String
) : EventSchema