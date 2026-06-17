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
 * Reads the contents of a locally stored file identified by [fileName]. The runner is currently
 * a placeholder (`println`) — the actual file read and trigger-firing logic has not yet been
 * implemented.
 *
 * **incomingData consumed:** Not used by the current placeholder runner. Once implemented,
 * [fileName] is the sole input; no incomingData is required.
 *
 * **Triggers fired (intended, not yet implemented):**
 * - [OnSuccessEventTrigger] — intended to fire after the file is read successfully, with the
 *   file contents (binary or text) available as incomingData for downstream events.
 * - [OnFailureEventTrigger] — intended to fire when the file cannot be read (e.g., file not
 *   found, permission denied, or I/O error).
 *
 * **Failure scenarios:** Not applicable — the runner is a no-op placeholder. When implemented,
 * the primary failure condition is [fileName] not existing in the app's private storage.
 *
 * **Notes:** [fileName] identifies the target file by name within the app's private storage
 * scope. The intended incomingData format for the success payload (raw bytes, Base64 string,
 * etc.) is not yet defined by the implementation.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("GetFile")
data class GetFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("fileName") val fileName: String
) : EventSchema
