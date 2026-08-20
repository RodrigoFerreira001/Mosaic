package dev.catbit.mosaic.server.builder.event.builders.image

import dev.catbit.mosaic.core.data.schemas.event.events.image.CompressionScheme
import dev.catbit.mosaic.core.data.schemas.event.events.image.ImageResizeOptions
import dev.catbit.mosaic.core.data.schemas.event.events.image.TakePictureEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

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
 * Opens the camera and emits the captured photo downstream, on the IO dispatcher. When
 * [compression] is set the bytes are re-encoded to WebP through [byQuality]/[byTargetSize] (the
 * capture is treated as `image/png`), with [resize] applied in the same pass (the compressor's
 * own defaults are used when [resize] is `null`); when [compression] is `null` the original
 * bytes are emitted untouched and [resize] has no effect. Does not consume `incomingData`.
 * Dispatches `onSuccess` (carrying the image, shaped by [outputType]) when a picture was taken
 * and processed; `onCancelled` (no data) when the camera returns nothing, e.g. the user backs out
 * of the capture; `onFailure` (carrying the thrown exception) when capturing or compressing
 * throws.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param compression Re-encoding applied to the captured image, built with [byQuality] or [byTargetSize]. Defaults to none (original bytes, [resize] has no effect).
 * @param resize Resize applied alongside [compression]; only takes effect when [compression] is non-null. Defaults to none (compressor's own defaults).
 * @param outputType Shape of the image delivered as `incomingData` — [pictureArrayOfBytes] or [pictureBase64]. Defaults to raw bytes.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onCancelled`, `onFailure`).
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

/** Re-encodes the image to WebP at [qualityPercent] (0-100). */
fun byQuality(qualityPercent: Float): CompressionScheme = CompressionScheme.ByQuality(qualityPercent)

/** Re-encodes the image to WebP, iterating quality to approximate [targetSizeKb]. */
fun byTargetSize(targetSizeKb: Int): CompressionScheme = CompressionScheme.ByTargetSize(targetSizeKb)

/** Delivers the captured image as a `ByteArray`. */
fun pictureArrayOfBytes(): TakePictureEventSchema.OutputType = TakePictureEventSchema.OutputType.ArrayOfBytes

/** Delivers the captured image as a base64-encoded `String`. */
fun pictureBase64(): TakePictureEventSchema.OutputType = TakePictureEventSchema.OutputType.Base64