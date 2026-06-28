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
 * - `onStart()` — file selected, bytes are being read
 * - `onSuccess(ByteArray)` — file bytes available as `incomingData`; chain with `UploadFile` or `SaveFile`
 * - `onFailure()` — user cancelled the picker or an exception occurred
 *
 * **Updatable fields (via UpdateEvents):** `fileType`, `pickMode`.
 *
 * @property fileType Restricts which files the picker shows. See [FileType] subtypes.
 * @property pickMode Controls single vs. multi selection. Currently only [PickMode.Single] is supported.
 */
@Immutable
@Serializable
@SerialName("OpenFilePicker")
data class OpenFilePickerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("fileType") val fileType: FileType,
    @SerialName("pickMode") val pickMode: PickMode
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
