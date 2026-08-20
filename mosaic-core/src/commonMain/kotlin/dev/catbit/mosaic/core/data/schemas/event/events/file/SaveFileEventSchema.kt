package dev.catbit.mosaic.core.data.schemas.event.events.file

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
 * Writes the event's incomingData to [fileName] in the client's own file storage.
 *
 * When [overrideIfExists] is `false` the runner first reads [fileName] and refuses to write if
 * something is already there.
 *
 * **incomingData consumed:** required, and must be a `ByteArray` — it is the content written.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the file was written. No data is passed downstream.
 * - `OnFailureEventTrigger` — when incomingData is missing or is not a `ByteArray` (an
 *   `IllegalArgumentException` is passed), when [overrideIfExists] is `false` and the file already
 *   exists (an `IllegalStateException` is passed), or when the write itself failed (the
 *   `Throwable` is passed). Every case is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
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
