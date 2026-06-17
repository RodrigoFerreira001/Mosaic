package dev.catbit.mosaic.core.data.schemas.event.events.event

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Patches the incomingData map of one or more already-registered event holders without
 * triggering a full server round-trip. For each entry in [updates] the runner calls
 * [tilesEventDispatcher.updateEventHolder], merging the provided [Update.data] map into the
 * target event's holder keyed by [Update.eventId].
 *
 * **incomingData consumed:** Not used by this event itself. The [Update.data] maps carried
 * inside [updates] become the new incomingData for the targeted event holders.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fired when all updates have been applied without error.
 * - [OnFailureEventTrigger] — fired when an error occurs during the update process
 *   (errorHappened flag is set); incomingData is the exception or null.
 *
 * **Failure scenarios:** If an [Update.eventId] does not match any registered holder the
 * update for that entry is silently skipped. No rollback occurs for already-applied updates
 * within the same [updates] list.
 *
 * **Notes:** This event is commonly used to pre-populate form fields or pass contextual data
 * into events before they fire — for example, loading a selected item's ID into a delete-event
 * holder. Updates are applied eagerly and synchronously in list order.
 */
@Immutable
@Triggers(
    [
        OnTilesUpdatedEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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