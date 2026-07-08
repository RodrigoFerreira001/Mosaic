package dev.catbit.mosaic.core.data.schemas.event.events.file

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Opens the system file picker, allowing the user to select a file.
 *
 * **Triggers fired:**
 * - `onStart()` — file selected, contents are being read (when [outputType] requires reading)
 * - `onSuccess(...)` — incomingData shaped according to [outputType]:
 *   - [FileOutputType.PlatformFile] — the picked `PlatformFile` reference (default); chain with
 *     `UploadFile` or `SaveFile`.
 *   - [FileOutputType.ArrayOfBytes] — the file contents as a [ByteArray].
 *   - [FileOutputType.FlowOfBytes] — a chunked `Flow<ByteArray>`, without loading the whole
 *     file into memory.
 *   - [FileOutputType.MapObject] — the file decoded as JSON into `Map<String, AnySerializable?>`.
 *   - [FileOutputType.Base64] — the file contents as a base64-encoded [String].
 * - `onFailure()` — user cancelled the picker, an exception occurred, or (when [outputType] is
 *   [FileOutputType.MapObject]) the file contents were not valid JSON
 *
 * **Updatable fields (via UpdateEvents):** `fileType`, `pickMode`.
 *
 * @property fileType Restricts which files the picker shows. See [FileType] subtypes.
 * @property pickMode Controls single vs. multi selection. Currently only [PickMode.Single] is supported.
 * @property outputType Shape of the data delivered as incomingData. Defaults to [FileOutputType.PlatformFile].
 */
@Immutable
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

        /** @property types MIME types or file extensions to filter (e.g. `"pdf", "png`). */
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
