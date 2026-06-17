package dev.catbit.mosaic.core.data.schemas.event.events.system

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
 * Checks whether the device currently has an active internet connection. The runner is currently
 * a placeholder (`println`) — the actual connectivity check and trigger-firing logic has not yet
 * been implemented.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired (intended, not yet implemented):**
 * - [OnSuccessEventTrigger] — intended to fire when an active internet connection is detected,
 *   with no incomingData payload.
 * - [OnFailureEventTrigger] — intended to fire when no internet connection is available, with
 *   no incomingData payload.
 *
 * **Failure scenarios:** Not applicable — the runner is a no-op placeholder.
 *
 * **Notes:** This event carries no extra fields beyond the base [EventSchema] contract. The
 * connectivity check strategy (e.g., DNS lookup, network callback) is platform-specific and
 * has not yet been decided or implemented.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("CheckIfHasInternetConnection")
data class CheckIfHasInternetConnectionEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema