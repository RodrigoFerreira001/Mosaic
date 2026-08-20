package dev.catbit.mosaic.core.data.schemas.event.events.file

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCancelledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Opens the platform file picker and emits the chosen file downstream.
 *
 * [fileType] restricts what can be picked: images, videos, both, or arbitrary files narrowed by a
 * list of extensions. [pickMode] currently only offers `Single`.
 *
 * [outputType] decides the shape handed to the next events:
 * - `PlatformFile` (default) — the platform file handle.
 * - `ArrayOfBytes` — the file read into a `ByteArray`.
 * - `FlowOfBytes` — a streaming flow of chunks, for large files.
 * - `MapObject` — the bytes decoded as a JSON map.
 * - `Base64` — the bytes encoded as a Base64 `String`.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when a file was picked and read; the content, in the shape chosen by
 *   [outputType], is passed as incomingData.
 * - `OnCancelledEventTrigger` — when the user dismisses the picker without choosing a file. No
 *   data is passed downstream.
 * - `OnFailureEventTrigger` — when `MapObject` decoding fails, or when the picker throws; the
 *   `Throwable` is passed as incomingData.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnCancelledEventTrigger::class,
    ]
)
@Serializable
@SerialName("OpenFilePicker")
data class OpenFilePickerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("fileType") val fileType: FileType,
    @SerialName("pickMode") val pickMode: PickMode,
    @SerialName("outputType") val outputType: FileOutputType = FileOutputType.PlatformFile
) : EventSchema {

    @Serializable
    sealed interface FileType {
        @Serializable
        @SerialName("Image")
        data object Image : FileType

        @Serializable
        @SerialName("Video")
        data object Video : FileType

        @Serializable
        @SerialName("ImageAndVideo")
        data object ImageAndVideo : FileType

        @Serializable
        @SerialName("File")
        data class File(val types: SerializableImmutableList<String>) : FileType
    }

    @Serializable
    sealed interface PickMode {
        @Serializable
        @SerialName("Single")
        data object Single : PickMode
    }
}
