package dev.catbit.mosaic.core.data.schemas.event.events.file

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Saves data to a local file on the device. The runner is currently a placeholder (`println`)
 * — the actual file write and trigger-firing logic has not yet been implemented.
 *
 * **incomingData consumed:** Not used by the current placeholder runner. Once implemented,
 * incomingData is expected to carry the binary or text content to be written to [fileName].
 *
 * **Triggers fired (intended, not yet implemented):**
 * - [OnSuccessEventTrigger] — intended to fire after the file is written successfully, with
 *   no incomingData payload.
 * - [OnFailureEventTrigger] — intended to fire when the write fails (e.g., permission denied,
 *   disk full, or the file already exists and [overrideIfExists] is false).
 *
 * **Failure scenarios:** Not applicable — the runner is a no-op placeholder. When implemented,
 * the primary failure condition is an existing file at [fileName] when [overrideIfExists] is
 * false, or any I/O exception during the write.
 *
 * **Notes:** [fileName] identifies the target file by name within the app's private storage
 * scope. [overrideIfExists] controls whether a pre-existing file with the same name should be
 * overwritten or treated as an error.
 */
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
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("fileName") val fileName: String,
    @SerialName("overrideIfExists") val overrideIfExists: Boolean
) : EventSchema
