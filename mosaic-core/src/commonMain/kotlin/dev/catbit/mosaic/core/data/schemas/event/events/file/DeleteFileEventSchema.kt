package dev.catbit.mosaic.core.data.schemas.event.events.file

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
 * Deletes a locally stored file from the device. The runner is currently a placeholder
 * (`println`) — the actual file deletion and trigger-firing logic has not yet been implemented.
 *
 * **incomingData consumed:** Not used by the current placeholder runner. Once implemented,
 * incomingData is expected to carry the file name or path of the file to delete — notably,
 * this schema has no [fileName] field of its own, so the target file must be supplied via
 * incomingData at runtime (e.g., injected by a prior [UpdateEventsEventSchema]).
 *
 * **Triggers fired (intended, not yet implemented):**
 * - [OnSuccessEventTrigger] — intended to fire after the file is deleted successfully, with
 *   no incomingData payload.
 * - [OnFailureEventTrigger] — intended to fire when the deletion fails (e.g., file not found,
 *   permission denied, or I/O error).
 *
 * **Failure scenarios:** Not applicable — the runner is a no-op placeholder.
 *
 * **Notes:** Unlike [GetFileEventSchema] and [SaveFileEventSchema], this schema declares no
 * [fileName] field. The file to be deleted must therefore be communicated via incomingData
 * injected before this event runs, or the implementation will need to derive the target from
 * the event's data map. This is a notable design asymmetry compared to the other file events.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("DeleteFile")
data class DeleteFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema
