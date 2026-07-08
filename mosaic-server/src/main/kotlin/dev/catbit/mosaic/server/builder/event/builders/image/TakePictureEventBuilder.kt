package dev.catbit.mosaic.server.builder.event.builders.image

import dev.catbit.mosaic.core.data.schemas.event.events.image.CompressionScheme
import dev.catbit.mosaic.core.data.schemas.event.events.image.ImageResizeOptions
import dev.catbit.mosaic.core.data.schemas.event.events.image.TakePictureEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

// ── CompressionScheme helpers ─────────────────────────────────────────────────

/** Re-encodes the image to WebP at [qualityPercent] (0-100). */
fun byQuality(qualityPercent: Float): CompressionScheme = CompressionScheme.ByQuality(qualityPercent)

/** Re-encodes the image to WebP, iterating quality to approximate [targetSizeKb]. */
fun byTargetSize(targetSizeKb: Int): CompressionScheme = CompressionScheme.ByTargetSize(targetSizeKb)

// ── TakePictureEventSchema.OutputType helpers ──────────────────────────────────

/** Delivers the captured image as a `ByteArray`. */
fun pictureArrayOfBytes(): TakePictureEventSchema.OutputType = TakePictureEventSchema.OutputType.ArrayOfBytes

/** Delivers the captured image as a base64-encoded `String`. */
fun pictureBase64(): TakePictureEventSchema.OutputType = TakePictureEventSchema.OutputType.Base64

// ── Builder ───────────────────────────────────────────────────────────────────

internal class TakePictureEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val compression: CompressionScheme?,
    private val resize: ImageResizeOptions?,
    private val outputType: TakePictureEventSchema.OutputType,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
) : EventSchemaBuilder<TakePictureEventSchema>() {

    override fun build() = TakePictureEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        compression = compression,
        resize = resize,
        outputType = outputType,
    )
}

/**
 * Opens the device camera, allowing the user to take a picture.
 *
 * **Triggers fired:**
 * - `onSuccess()` — picture captured; captured image available as `incomingData`, shaped
 *   according to [outputType]
 * - `onFailure()` — user cancelled the capture or an exception occurred
 *
 * @param compression When null, the raw captured image bytes are returned as-is (original
 *   format). When set, the image is re-encoded as **WebP**. Use [byQuality] or [byTargetSize].
 * @param resize Only applies when [compression] is non-null. If null, uses the compression
 *   library's own default.
 * @param outputType Shape of the captured image delivered as `incomingData`. Use
 *   [pictureArrayOfBytes] or [pictureBase64].
 */
fun EventSchemaBuilderScope.TakePicture(
    id: String = randomId(),
    trigger: EventTrigger,
    compression: CompressionScheme? = null,
    resize: ImageResizeOptions? = null,
    outputType: TakePictureEventSchema.OutputType = pictureArrayOfBytes(),
    events: EventSchemaBuilderScope.() -> Unit = {},
) {
    addBuilder(
        TakePictureEventBuilder(
            id = id,
            trigger = trigger,
            compression = compression,
            resize = resize,
            outputType = outputType,
            events = events,
        )
    )
}
