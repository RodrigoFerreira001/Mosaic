package dev.catbit.mosaic.core.data.schemas.event.events.time

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerTickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeFinishEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Starts a countdown in a coroutine launched from the running event's context, ticking down from
 * [TimerData] `initial` to its `step` in `step`-sized decrements, waiting one step between ticks.
 * [TimerData] chooses the unit: `Milliseconds` or `Seconds`.
 *
 * The event returns as soon as the countdown is launched, so the chain continues while the timer
 * keeps running in the background. It stops on its own when the countdown ends; to stop it
 * early, launch it from inside a `RunCancellableEvents` and cancel that id with `CancelEvents`.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnCountdownTimerTickEventTrigger` — once per tick, with the remaining amount passed as
 *   incomingData.
 * - `OnTimeFinishEventTrigger` — once, after the last tick. No data is passed downstream.
 *
 * Neither success nor failure is reported.
 */
@Immutable
@Triggers(
    [
        OnCountdownTimerTickEventTrigger::class,
        OnTimeFinishEventTrigger::class,
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
