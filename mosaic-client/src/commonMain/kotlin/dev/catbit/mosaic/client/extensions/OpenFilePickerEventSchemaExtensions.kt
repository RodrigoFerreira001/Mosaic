package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType

/** Converts `OpenFilePicker`'s `fileType` into the `filekit` library's own [FileKitType], which
 * `OpenFilePickerEventRunner` passes to the platform's native file picker dialog. */
fun OpenFilePickerEventSchema.FileType.toFileKitType() = when (this) {
    is OpenFilePickerEventSchema.FileType.File -> FileKitType.File(types)
    OpenFilePickerEventSchema.FileType.Image -> FileKitType.Image
    OpenFilePickerEventSchema.FileType.ImageAndVideo -> FileKitType.ImageAndVideo
    OpenFilePickerEventSchema.FileType.Video -> FileKitType.Video
}

/** Converts `OpenFilePicker`'s `pickMode` into the `filekit` library's own [FileKitMode]. Only
 * `Single` exists on the schema today, matching `filekit`'s own `FileKitMode.Single`. */
fun OpenFilePickerEventSchema.PickMode.toFileKitMode() = when (this) {
    OpenFilePickerEventSchema.PickMode.Single -> FileKitMode.Single
}