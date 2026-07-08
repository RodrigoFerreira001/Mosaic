package dev.catbit.mosaic.server.builder.event.builders.image

import dev.catbit.mosaic.core.data.schemas.event.events.image.CompressionScheme
import dev.catbit.mosaic.core.data.schemas.event.events.image.GetImageFromGalleryEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.image.ImageResizeOptions
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

// ── GetImageFromGalleryEventSchema.OutputType helpers ──────────────────────────

/** Delivers the picked image as a `ByteArray`. */
fun galleryArrayOfBytes(): GetImageFromGalleryEventSchema.OutputType = GetImageFromGalleryEventSchema.OutputType.ArrayOfBytes

/** Delivers the picked image as a base64-encoded `String`. */
fun galleryBase64(): GetImageFromGalleryEventSchema.OutputType = GetImageFromGalleryEventSchema.OutputType.Base64

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
 * Opens the device gallery, allowing the user to pick an image.
 *
 * **Triggers fired:**
 * - `onSuccess()` — image selected; picked image available as `incomingData`, shaped according
 *   to [outputType]
 * - `onFailure()` — user cancelled the selection or an exception occurred
 *
 * @param compression When null, the raw picked image bytes are returned as-is (original
 *   format). When set, the image is re-encoded as **WebP**. Use [byQuality] or [byTargetSize].
 * @param resize Only applies when [compression] is non-null. If null, uses the compression
 *   library's own default.
 * @param outputType Shape of the picked image delivered as `incomingData`. Use
 *   [galleryArrayOfBytes] or [galleryBase64].
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
