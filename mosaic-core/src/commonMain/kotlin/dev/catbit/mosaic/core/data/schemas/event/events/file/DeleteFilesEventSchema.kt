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
 * Deletes the file or files selected by [target] from the client's own file storage. With a
 * [DeleteTarget.Multiple] every file is attempted, even after one fails.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when every deletion completed. No data is passed downstream.
 * - `OnFailureEventTrigger` — when at least one deletion failed, fired once after all were attempted;
 *   the first `Throwable` is passed as incomingData and every error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("DeleteFiles")
data class DeleteFilesEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("target") val target: DeleteTarget
) : EventSchema {

    /** Which file or files [DeleteFilesEventSchema] deletes. */
    @Serializable
    sealed interface DeleteTarget {
        /** Deletes the one file stored under [fileName]. */
        @Serializable
        @SerialName("Single")
        data class Single(val fileName: String) : DeleteTarget

        /** Deletes every file in [fileNames]. */
        @Serializable
        @SerialName("Multiple")
        data class Multiple(val fileNames: SerializableImmutableList<String>) : DeleteTarget
    }
}
