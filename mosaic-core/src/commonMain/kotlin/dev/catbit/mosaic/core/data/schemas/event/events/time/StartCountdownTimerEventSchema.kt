package dev.catbit.mosaic.core.data.schemas.event.events.time

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerTickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Starts a client-side countdown timer that counts down from [setupTimeInSeconds] to zero.
 * The runner is currently a placeholder (`println`) — the countdown and trigger-firing logic
 * has not yet been implemented.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired (intended, not yet implemented):**
 * - [OnCountdownTimerTickEventTrigger] — intended to fire on every elapsed second, with the
 *   remaining time as incomingData.
 * - [OnCountdownTimerFinishEventTrigger] — intended to fire once when the timer reaches zero.
 *
 * **Failure scenarios:** Not applicable — the runner is a no-op placeholder.
 *
 * **Notes:** The runner body only prints a debug message. No actual timer is started, no
 * coroutine is launched, and no triggers are dispatched until this event is fully implemented.
 * [setupTimeInSeconds] must be a positive value; the intended behavior when zero or negative
 * is provided is undefined.
 */
@Immutable
@Triggers(
    [
        OnCountdownTimerFinishEventTrigger::class,
        OnCountdownTimerTickEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("StartCountdownTimer")
data class StartCountdownTimerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("setupTimeInSeconds") val setupTimeInSeconds: Long
) : EventSchema
