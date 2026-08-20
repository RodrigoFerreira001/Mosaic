package dev.catbit.mosaic.core.data.schemas.event.events.event

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Patches events that are already registered on the screen. Each [Update] targets an event by
 * [Update.eventId] and merges [Update.data] into it, the same way `UpdateTiles` patches a tile, so
 * a chain can rewrite another event's parameters before it runs.
 *
 * All updates are attempted; a failure in one does not stop the others.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when every update was applied. No data is passed downstream.
 * - `OnFailureEventTrigger` — when at least one update failed, typically because no event carries
 *   that id. Fired once at the end, after all updates were attempted, with no data attached.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("UpdateEvents")
data class UpdateEventsEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("updates") val updates: SerializableImmutableList<Update>,
) : EventSchema {

    @Serializable
    data class Update(
        @SerialName("eventId") val eventId: String,
        @SerialName("data") val data: Map<String, AnySerializable?>
    )
}