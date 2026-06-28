package dev.catbit.mosaic.client.extensions

import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType

fun OpenFilePickerEventSchema.FileType.toFileKitType() = when (this) {
    is OpenFilePickerEventSchema.FileType.File -> FileKitType.File(types)
    OpenFilePickerEventSchema.FileType.Image -> FileKitType.Image
    OpenFilePickerEventSchema.FileType.ImageAndVideo -> FileKitType.ImageAndVideo
    OpenFilePickerEventSchema.FileType.Video -> FileKitType.Video
}

fun OpenFilePickerEventSchema.PickMode.toFileKitMode() = when (this) {
    OpenFilePickerEventSchema.PickMode.Single -> FileKitMode.Single
}