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
 * Opens the platform file picker and emits the chosen file downstream, shaped by [outputType].
 * [fileType] restricts what can be picked; [pickMode] currently only offers a single selection.
 * Does not consume `incomingData`. Dispatches `onSuccess` (carrying the content, shaped by
 * [outputType]) when a file was picked and read; `onCancelled` (no data) when the user dismisses
 * the picker without choosing a file; `onFailure` (carrying the thrown exception) when
 * `mapObject()` decoding fails or the picker itself throws.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param fileType Kinds of files selectable in the picker — [imageFileType], [videoFileType], [imageAndVideoFileType] or [fileFileType].
 * @param pickMode Selection mode — currently only [singlePickMode]. Defaults to single.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onCancelled`, `onFailure`).
 * @param outputType Shape of the content delivered as `incomingData` — [platformFile], [arrayOfBytes], [flowOfBytes], [mapObject] or [base64]. Defaults to the platform file handle.
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
