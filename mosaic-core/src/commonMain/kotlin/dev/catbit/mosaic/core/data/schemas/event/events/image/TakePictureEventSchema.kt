package dev.catbit.mosaic.core.data.schemas.event.events.image

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCancelledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Opens the camera through the client's `CameraManager` and emits the captured photo downstream.
 * Runs on the IO dispatcher.
 *
 * When [compression] is set the bytes are re-encoded through the client's `ImageCompressor` (the
 * capture is treated as `image/png`), with [resize] applied as part of the same pass (default
 * resize options are used when [resize] is `null`). When [compression] is `null` the original
 * bytes are emitted untouched and [resize] has no effect.
 *
 * [outputType] decides the shape handed to the next events: `ArrayOfBytes` or a Base64 `String`.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when a picture was taken and processed; the image, in the shape
 *   chosen by [outputType], is passed as incomingData.
 * - `OnCancelledEventTrigger` — when the camera returns nothing, such as the user backing out of
 *   the capture. No data is passed downstream.
 * - `OnFailureEventTrigger` — when capturing or compressing throws; the `Throwable` is passed as
 *   incomingData.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnCancelledEventTrigger::class,
    ]
)
@Serializable
@SerialName("TakePicture")
data class TakePictureEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("compression") val compression: CompressionScheme?,
    @SerialName("resize") val resize: ImageResizeOptions?,
    @SerialName("outputType") val outputType: OutputType
) : EventSchema {

    @Serializable
    enum class OutputType {
        @SerialName("ArrayOfBytes")
        ArrayOfBytes,

        @SerialName("Base64")
        Base64,
    }
}
