package dev.catbit.mosaic.core.data.schemas.event.events.time

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeLoopEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Starts a client-side, unbounded loop that repeats every [TimeData.delay] — unlike
 * [StartCountdownTimerEventSchema], there is no end condition; stop it externally (e.g. via
 * [dev.catbit.mosaic.core.data.schemas.event.events.event.CancelEventsEventSchema] once wired
 * through a cancellable context). Build [timeData] with the `milliseconds(delay)` /
 * `seconds(delay)` DSL helpers (`dev.catbit.mosaic.server.builder.event.builders.time`), which
 * validate that `delay` is positive.
 * The runner is currently a placeholder — the loop and trigger-firing logic has not yet been
 * implemented.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired (intended, not yet implemented):**
 * - [OnTimeLoopEventTrigger] (`EventTriggers.onTimeLoop()`) — intended to fire on every elapsed
 *   [TimeData.delay].
 * - [OnSuccessEventTrigger] — intended to fire once when the loop starts.
 *
 * **Failure scenarios:** Not applicable — the runner is a no-op placeholder.
 */
@Immutable
@Triggers(
    [
        OnTimeLoopEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("StartTimeLoop")
data class StartTimeLoopEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("timeData") val timeData: TimeData
) : EventSchema {

    @Serializable
    sealed interface TimeData {

        @Serializable
        @SerialName("Milliseconds")
        data class Milliseconds(
            @SerialName("delay") val delay: Long
        ) : TimeData

        @Serializable
        @SerialName("Seconds")
        data class Seconds(
            @SerialName("delay") val delay: Int
        ) : TimeData
    }
}
