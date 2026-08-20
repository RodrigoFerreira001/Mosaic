package dev.catbit.mosaic.core.data.schemas.event.events.file

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
 * Reads the file stored under [fileName] from the client's own file storage and emits its
 * content downstream.
 *
 * [outputType] decides the shape handed to the next events:
 * - `ArrayOfBytes` (default) — the raw `ByteArray`.
 * - `FlowOfBytes` — a streaming flow of chunks, for large files.
 * - `PlatformFile` — the platform file handle.
 * - `MapObject` — the bytes decoded as a JSON map.
 * - `Base64` — the bytes encoded as a Base64 `String`.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the file was read; the content, in the shape chosen by
 *   [outputType], is passed as incomingData.
 * - `OnFailureEventTrigger` — when the read failed, when no file exists under [fileName] (a
 *   `NoSuchElementException` is passed), or when `MapObject` decoding failed. The `Throwable` is
 *   passed as incomingData and the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
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
