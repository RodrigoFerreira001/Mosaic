package dev.catbit.mosaic.core.data.schemas.event.events.file

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Saves data to a local file on the device.
 *
 * **incomingData consumed:** Expected to be a [ByteArray] with the content to be written to
 * [fileName].
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fires after the file is written successfully, with no
 *   incomingData payload.
 * - [OnFailureEventTrigger] — fires when the write fails: incomingData isn't a [ByteArray],
 *   the file already exists and [overrideIfExists] is false, or any I/O exception during the
 *   write. The [Throwable] is passed as incomingData.
 *
 * **Notes:** [fileName] identifies the target file by name within the app's private storage
 * scope. [overrideIfExists] controls whether a pre-existing file with the same name should be
 * overwritten or treated as an error.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("SaveFile")
data class SaveFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("fileName") val fileName: String,
    @SerialName("overrideIfExists") val overrideIfExists: Boolean
) : EventSchema
