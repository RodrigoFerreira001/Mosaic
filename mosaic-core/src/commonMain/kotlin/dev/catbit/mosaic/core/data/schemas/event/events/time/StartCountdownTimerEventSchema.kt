package dev.catbit.mosaic.core.data.schemas.event.events.time

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerTickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeFinishEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Starts a client-side countdown timer that counts down from [TimerData.initial] to zero in
 * decrements of [TimerData.step]. Build [timerData] with the `milliseconds(initial, step)` /
 * `seconds(initial, step)` DSL helpers (`dev.catbit.mosaic.server.builder.event.builders.time`),
 * which validate that `step` is non-negative and smaller than `initial`.
 * The runner is currently a placeholder — the countdown and trigger-firing logic has not yet
 * been implemented.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired (intended, not yet implemented):**
 * - [OnCountdownTimerTickEventTrigger] (`EventTriggers.onTimeTick()`) — intended to fire on every
 *   elapsed [TimerData.step], with the remaining time as incomingData.
 * - [OnTimeFinishEventTrigger] (`EventTriggers.onTimeFinish()`) — intended to fire once when the
 *   timer reaches zero.
 *
 * **Failure scenarios:** Not applicable — the runner is a no-op placeholder.
 */
@Immutable
@Triggers(
    [
        OnTimeFinishEventTrigger::class,
        OnCountdownTimerTickEventTrigger::class
    ]
)
@Serializable
@SerialName("StartCountdownTimer")
data class StartCountdownTimerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("timerData") val timerData: TimerData
) : EventSchema {

    @Serializable
    sealed interface TimerData {
        val initial: Number
        val step: Number

        @Serializable
        @SerialName("Milliseconds")
        data class Milliseconds(
            @SerialName("initial") override val initial: Long,
            @SerialName("step") override val step: Long
        ) : TimerData

        @Serializable
        @SerialName("Seconds")
        data class Seconds(
            @SerialName("initial") override val initial: Int,
            @SerialName("step") override val step: Int
        ) : TimerData
    }
}
