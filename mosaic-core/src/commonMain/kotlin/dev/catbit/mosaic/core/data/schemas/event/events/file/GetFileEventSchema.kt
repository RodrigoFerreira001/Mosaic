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
 * Reads the contents of a locally stored file identified by [fileName].
 *
 * **incomingData consumed:** Not used; [fileName] is the sole input.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fires after the file is read successfully, with incomingData
 *   shaped according to [outputType]:
 *   - [FileOutputType.ArrayOfBytes] — raw file contents as a [ByteArray] (default).
 *   - [FileOutputType.FlowOfBytes] — a chunked `Flow<ByteArray>`, without loading the whole
 *     file into memory.
 *   - [FileOutputType.PlatformFile] — a reference to the file (`PlatformFile`), without
 *     reading its contents.
 *   - [FileOutputType.MapObject] — the file decoded as JSON into `Map<String, AnySerializable?>`.
 *   - [FileOutputType.Base64] — the file contents as a base64-encoded [String].
 * - [OnFailureEventTrigger] — fires when the file cannot be read (file not found, I/O error,
 *   or invalid JSON when [outputType] is [FileOutputType.MapObject]). The [Throwable] is
 *   passed as incomingData.
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
@SerialName("GetFile")
data class GetFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("fileName") val fileName: String,
    @SerialName("outputType") val outputType: FileOutputType = FileOutputType.ArrayOfBytes
) : EventSchema
