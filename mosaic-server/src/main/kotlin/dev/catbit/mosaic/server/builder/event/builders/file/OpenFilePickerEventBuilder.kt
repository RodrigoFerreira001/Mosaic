package dev.catbit.mosaic.server.builder.event.builders.file

import dev.catbit.mosaic.core.data.schemas.event.events.file.FileOutputType
import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema.FileType
import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema.PickMode
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList

// ── FileType helpers ──────────────────────────────────────────────────────────

/** Allows the user to pick image files only. */
fun imageFileType(): FileType = FileType.Image

/** Allows the user to pick video files only. */
fun videoFileType(): FileType = FileType.Video

/** Allows the user to pick images or videos. */
fun imageAndVideoFileType(): FileType = FileType.ImageAndVideo

/**
 * Allows the user to pick files matching the given MIME types or extensions.
 *
 * Example: `fileFileType("pdf", "png", "txt")`
 */
fun fileFileType(vararg types: String): FileType =
    FileType.File(types = types.toList().toImmutableList() as SerializableImmutableList<String>)

// ── PickMode helpers ──────────────────────────────────────────────────────────

/** The user can select a single file. */
fun singlePickMode(): PickMode = PickMode.Single

// ── Builder ───────────────────────────────────────────────────────────────────

internal class OpenFilePickerEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val fileType: FileType,
    private val pickMode: PickMode,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val outputType: FileOutputType,
) : EventSchemaBuilder<OpenFilePickerEventSchema>() {

    override fun build() = OpenFilePickerEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        fileType = fileType,
        pickMode = pickMode,
        outputType = outputType,
    )
}

/**
 * Opens the system file picker, allowing the user to select a file.
 *
 * **Triggers fired:**
 * - `onStart()` — file selected, contents are being read (when [outputType] requires reading)
 * - `onSuccess(...)` — incomingData shaped according to [outputType]
 * - `onFailure()` — user cancelled the picker or an exception occurred
 *
 * @param fileType The type of files to show in the picker. Use [imageFileType], [videoFileType],
 *   [imageAndVideoFileType], or [fileFileType].
 * @param pickMode Selection mode. Defaults to [singlePickMode].
 * @param outputType Shape of the data delivered as incomingData. Use [platformFile] (default),
 *   [arrayOfBytes], [flowOfBytes], or [mapObject].
 */
fun EventSchemaBuilderScope.OpenFilePicker(
    id: String = randomId(),
    trigger: EventTrigger,
    fileType: FileType,
    pickMode: PickMode = singlePickMode(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    outputType: FileOutputType = platformFile(),
) {
    addBuilder(
        OpenFilePickerEventBuilder(
            id = id,
            trigger = trigger,
            fileType = fileType,
            pickMode = pickMode,
            events = events,
            outputType = outputType,
        )
    )
}
