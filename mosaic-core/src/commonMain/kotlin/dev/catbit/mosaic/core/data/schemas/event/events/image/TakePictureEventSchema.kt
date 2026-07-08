package dev.catbit.mosaic.core.data.schemas.event.events.image

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Opens the device camera, allowing the user to take a picture.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — picture captured; captured image available as `incomingData`,
 *   shaped according to [outputType].
 * - [OnFailureEventTrigger] — user cancelled the capture or an exception occurred.
 *
 * @property compression When null, the raw captured image bytes are returned as-is (original
 *   format). When set, the image is re-encoded as **WebP** using this compression strategy.
 * @property resize Only applies when [compression] is non-null. Null uses the compression
 *   library's own default.
 * @property outputType Controls the shape of the captured image delivered as `incomingData`.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
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

    /** Controls the shape of the data delivered as incomingData by [TakePictureEventSchema]. */
    @Serializable
    enum class OutputType {
        /** Delivers the captured image as a [ByteArray]. */
        @SerialName("ArrayOfBytes")
        ArrayOfBytes,

        /** Delivers the captured image as a base64-encoded [String]. */
        @SerialName("Base64")
        Base64,
    }
}
