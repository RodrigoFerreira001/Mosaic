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
 * Runs this event's own `events` list inline, in order, each receiving this event's incomingData.
 * It is the way to fan a single trigger out into several events, or to group events for reuse.
 *
 * Unlike most events, `events` here is the payload to execute rather than a downstream chain.
 * Each entry is run guarded, so one failing event is logged and the remaining ones still run.
 *
 * **incomingData consumed:** forwarded unchanged to every event in the list.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when every event in the list ran without throwing. No data is
 *   passed downstream.
 * - `OnFailureEventTrigger` — when at least one of them threw. Fired once at the end, after all
 *   of them were attempted, with no data attached; each failure is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("RunEvents")
data class RunEventsEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema
