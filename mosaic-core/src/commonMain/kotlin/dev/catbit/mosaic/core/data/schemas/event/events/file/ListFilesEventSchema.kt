package dev.catbit.mosaic.core.data.schemas.event.events.file

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnEmptyDirEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Lists the files directly inside [directory] in the client's own file storage; subdirectories and
 * the files inside them are skipped. Each returned path is relative to the storage root — the same form
 * `GetFile` and `DeleteFiles` take as `fileName` — so it can be passed straight to them. Not recursive.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the directory holds at least one file; the `List<String>` of
 *   file paths is passed as incomingData.
 * - `OnEmptyDirEventTrigger` — when the directory exists but holds no files. No data is passed
 *   downstream.
 * - `OnFailureEventTrigger` — when the directory doesn't exist (the cause is a
 *   `DirNotFoundException`) or the listing failed; the `Throwable` is passed as incomingData and
 *   the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnEmptyDirEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("ListFiles")
data class ListFilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("directory") val directory: String
) : EventSchema
