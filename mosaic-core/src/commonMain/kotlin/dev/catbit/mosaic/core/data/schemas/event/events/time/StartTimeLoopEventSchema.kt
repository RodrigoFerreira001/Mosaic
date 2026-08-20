package dev.catbit.mosaic.core.data.schemas.event.events.time

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeLoopEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Starts an endless loop in a coroutine launched from the running event's context, firing once
 * per period. [TimeData] sets the period and its unit: `Milliseconds` or `Seconds`. The first fire
 * happens after the first delay, not immediately.
 *
 * The event returns as soon as the loop is launched, so the chain continues while it keeps
 * running. The loop runs forever by design: to be able to stop it, launch it from inside a
 * `RunCancellableEvents` and cancel that id with `CancelEvents`.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnTimeLoopEventTrigger` — once per period, indefinitely. No data is passed downstream.
 *
 * Neither success nor failure is reported.
 */
@Immutable
@Triggers([OnTimeLoopEventTrigger::class])
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
