package dev.catbit.mosaic.server.builder.event.builders.image

import dev.catbit.mosaic.core.data.schemas.event.events.image.CompressionScheme
import dev.catbit.mosaic.core.data.schemas.event.events.image.GetImageFromGalleryEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.image.ImageResizeOptions
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class GetImageFromGalleryEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val compression: CompressionScheme?,
    private val resize: ImageResizeOptions?,
    private val outputType: GetImageFromGalleryEventSchema.OutputType,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<GetImageFromGalleryEventSchema>() {

    override fun build() = GetImageFromGalleryEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        compression = compression,
        resize = resize,
        outputType = outputType,
    )
}

/**
 * Opens the platform picker restricted to images and emits the chosen image downstream, on the
 * IO dispatcher. When [compression] is set the bytes are re-encoded to WebP through
 * [byQuality]/[byTargetSize], with [resize] applied in the same pass (the compressor's own
 * defaults are used when [resize] is `null`); when [compression] is `null` the original bytes
 * are emitted untouched and [resize] has no effect. Does not consume `incomingData`. Dispatches
 * `onSuccess` (carrying the image, shaped by [outputType]) when an image was picked and
 * processed; `onCancelled` (no data) when the user dismisses the picker without choosing an
 * image; `onFailure` (carrying the thrown exception) when reading or compressing throws.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param compression Re-encoding applied to the picked image, built with [byQuality] or [byTargetSize]. Defaults to none (original bytes, [resize] has no effect).
 * @param resize Resize applied alongside [compression]; only takes effect when [compression] is non-null. Defaults to none (compressor's own defaults).
 * @param outputType Shape of the image delivered as `incomingData` — [galleryArrayOfBytes] or [galleryBase64]. Defaults to raw bytes.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onCancelled`, `onFailure`).
 */
fun EventSchemaBuilderScope.GetImageFromGallery(
    id: String = randomId(),
    trigger: EventTrigger,
    compression: CompressionScheme? = null,
    resize: ImageResizeOptions? = null,
    outputType: GetImageFromGalleryEventSchema.OutputType = galleryArrayOfBytes(),
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        GetImageFromGalleryEventBuilder(
            id = id,
            trigger = trigger,
            compression = compression,
            resize = resize,
            outputType = outputType,
            events = events,
        )
    )
}

/** Delivers the picked image as a `ByteArray`. */
fun galleryArrayOfBytes(): GetImageFromGalleryEventSchema.OutputType = GetImageFromGalleryEventSchema.OutputType.ArrayOfBytes

/** Delivers the picked image as a base64-encoded `String`. */
fun galleryBase64(): GetImageFromGalleryEventSchema.OutputType = GetImageFromGalleryEventSchema.OutputType.Base64
