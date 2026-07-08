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
 * Deletes a locally stored file from the device identified by [fileName].
 *
 * **incomingData consumed:** Not used; [fileName] is the sole input.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fires after the file is deleted successfully, with no
 *   incomingData payload.
 * - [OnFailureEventTrigger] — fires when the deletion fails due to any I/O exception. The
 *   [Throwable] is passed as incomingData.
 *
 * **Notes:** [fileName] identifies the target file by name within the app's private storage
 * scope.
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
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("fileName") val fileName: String
) : EventSchema
