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
 * Looks up the event registered on the screen under [eventId] and runs it inline, letting one
 * event chain re-use another that lives on a different tile.
 *
 * **incomingData consumed:** forwarded unchanged to the event being run.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — after the target event ran. No data is passed downstream.
 * - `OnFailureEventTrigger` — when no event is registered under [eventId] (no data passed), or
 *   when running it throws (the `Throwable` is passed as incomingData). Both cases are logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
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